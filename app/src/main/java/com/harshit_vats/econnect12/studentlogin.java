package com.harshit_vats.econnect12;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class studentlogin extends AppCompatActivity {
    private TextView heading;
    private EditText emailInput, passwordInput, classInput, sectionInput, rollNumber;
    private Button loginButton;
    private ImageView logo;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Animation topAnim, bottomAnim, leftAnim, rightAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);

        // Initialize UI elements
        logo = findViewById(R.id.logo);
        heading = findViewById(R.id.heading);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginBtn);
        classInput = findViewById(R.id.classInput);
        sectionInput = findViewById(R.id.sectionInput);
        rollNumber = findViewById(R.id.rollNumber);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Load animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_annimation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_annimation);

        // Apply animations
        heading.setAnimation(topAnim);
        loginButton.setAnimation(bottomAnim);
        emailInput.setAnimation(rightAnim);
        passwordInput.setAnimation(leftAnim);
        classInput.setAnimation(leftAnim);
        sectionInput.setAnimation(rightAnim);
        rollNumber.setAnimation(leftAnim);

        // Set click listener for login button
        loginButton.setOnClickListener(v -> loginStudent());
    }

    private void loginStudent() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String studentClass = classInput.getText().toString().trim();
        String section = sectionInput.getText().toString().trim();
        String rollText = rollNumber.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || studentClass.isEmpty() || section.isEmpty() || rollText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int roll;
        try {
            roll = Integer.parseInt(rollText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Roll Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            // 🔥 Fetch student details from Firestore using class -> section -> roll_number
                            DocumentReference studentRef = db.collection("Students")
                                    .document(studentClass)
                                    .collection(section)
                                    .document(String.valueOf(roll));

                            studentRef.get()
                                    .addOnSuccessListener(document -> {
                                        if (document.exists()) {
                                            String storedUid = document.getString("uid");
                                            if (storedUid != null && storedUid.equals(uid)) {
                                                String name = document.getString("name");
                                                Toast.makeText(this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                                                // Redirect to student dashboard
                                                Intent intent = new Intent(this, StudentDashboardActivitty.class);
                                                intent.putExtra("studentName", name);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(this, "Invalid Roll Number or Class!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "No student found with these details!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
