package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterStudents extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText admissionNumber, studentName, classInput, sectionInput, rollNumber, emailInput, passwordInput;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_students);
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
        registerButton.setOnClickListener(v -> registerStudent());




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void registerStudent() {
        // Retrieve values from input fields
        String admissionId = admissionNumber.getText().toString().trim();  // Admin-assigned ID
        String name = studentName.getText().toString().trim();
        String studentClass = classInput.getText().toString().trim();
        String section = sectionInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String rollText = rollNumber.getText().toString().trim();

        // Validate input fields
        if (admissionId.isEmpty() || name.isEmpty() || studentClass.isEmpty() || section.isEmpty() || email.isEmpty() || password.isEmpty() || rollText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Firestore reference to /Students/{admissionId}
        DocumentReference studentRef = db.collection("Students").document(admissionId);

        // Step 1: Check if Student ID Exists Before Registering
        studentRef.get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        Toast.makeText(this, "Student ID already exists!", Toast.LENGTH_SHORT).show();
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
                                                            String uid = user.getUid();  // Firebase Authentication UID

                                                            // Create Firestore Entry
                                                            Map<String, Object> student = new HashMap<>();
                                                            student.put("uid", uid);
                                                            student.put("name", name);
                                                            student.put("email", email);
                                                            student.put("password", password);
                                                            student.put("class", studentClass);
                                                            student.put("section", section);
                                                            student.put("roll_number", roll);

                                                            // Store student details at /Students/{admissionId}
                                                            studentRef.set(student)
                                                                    .addOnSuccessListener(aVoid -> {
                                                                        Toast.makeText(this, "Student Registered Successfully!", Toast.LENGTH_SHORT).show();
                                                                        clearFields();
                                                                    })
                                                                    .addOnFailureListener(e -> {
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