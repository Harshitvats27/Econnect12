package com.harshit_vats.econnect12;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText admissionNumber, studentName, classInput, sectionInput, rollNumber, emailInput, passwordInput;
    private Button registerButton,RegisterButtonfacylty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        admissionNumber = findViewById(R.id.admissionNumber);
        studentName = findViewById(R.id.studentName);
        classInput = findViewById(R.id.classInput);
        sectionInput = findViewById(R.id.sectionInput);
        rollNumber = findViewById(R.id.rollNumber);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        RegisterButtonfacylty=findViewById(R.id.registerButtonfaculty);

        registerButton.setOnClickListener(v -> registerStudent());

        RegisterButtonfacylty.setOnClickListener(v -> {
           Intent it = new Intent(AdminDashboardActivity.this,RegisterFaculty.class);
           startActivity(it);
           finish();
        });
    }


    private void registerStudent() {
        String admissionId = admissionNumber.getText().toString().trim();
        String name = studentName.getText().toString().trim();
        String studentClass = classInput.getText().toString().trim();
        String section = sectionInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String rollText = rollNumber.getText().toString().trim();

        if (admissionId.isEmpty() || name.isEmpty() || studentClass.isEmpty() || section.isEmpty() || email.isEmpty() || password.isEmpty() || rollText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password length
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        int roll;
        try {
            roll = Integer.parseInt(rollText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Roll Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference sectionRef = db.collection("Students")
                .document(studentClass)
                .collection(section);

        // Step 1: Check if Roll Number Exists Before Registering User
        sectionRef.document(String.valueOf(roll)).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        Toast.makeText(this, "Roll number already exists in this section!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Step 2: Check if Email is Already Registered
                        auth.fetchSignInMethodsForEmail(email)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().getSignInMethods().isEmpty()) {
                                        Toast.makeText(this, "Email is already registered!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Step 3: Register User in Firebase Authentication
                                        auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(authTask -> {
                                                    if (authTask.isSuccessful()) {
                                                        FirebaseUser user = authTask.getResult().getUser();
                                                        if (user != null) {
                                                            String uid = user.getUid();

                                                            // Create Firestore Entry
                                                            Map<String, Object> student = new HashMap<>();
                                                            student.put("admission_number", admissionId);
                                                            student.put("name", name);
                                                            student.put("email", email);
                                                            student.put("roll_number", roll);
                                                            student.put("uid", uid); // Store Firebase UID

                                                            // Step 4: Use Firestore Transaction to Prevent Data Overwriting
                                                            WriteBatch batch = db.batch();
                                                            batch.set(sectionRef.document(String.valueOf(roll)), student);

                                                            batch.commit().addOnSuccessListener(aVoid -> {
                                                                Toast.makeText(this, "Student Registered Successfully!", Toast.LENGTH_SHORT).show();
                                                                clearFields();
                                                            }).addOnFailureListener(e -> {
                                                                Toast.makeText(this, "Failed to Register Student in Firestore!", Toast.LENGTH_SHORT).show();
                                                                e.printStackTrace();
                                                            });
                                                        }
                                                    } else {
                                                        // Handle Firebase Auth Errors
                                                        if (authTask.getException() instanceof FirebaseAuthWeakPasswordException) {
                                                            Toast.makeText(this, "Weak Password: Must be 6+ characters!", Toast.LENGTH_SHORT).show();
                                                        } else if (authTask.getException() instanceof FirebaseAuthUserCollisionException) {
                                                            Toast.makeText(this, "Email already in use!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(this, "Registration Failed: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error checking email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

    private void clearFields() {
        admissionNumber.setText("");
        studentName.setText("");
        classInput.setText("");
        sectionInput.setText("");
        rollNumber.setText("");
        emailInput.setText("");
        passwordInput.setText("");
    }
}
