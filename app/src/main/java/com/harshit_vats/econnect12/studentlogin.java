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
    private EditText emailInput, passwordInput, admissionNumberInput;
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
        heading = findViewById(R.id.welcomeText); // FIXED
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        admissionNumberInput = findViewById(R.id.admissionNumberInput);
        loginButton = findViewById(R.id.loginBtn);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Load animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_annimation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_annimation);

        // Apply animations safely
        if (heading != null) {
            heading.setAnimation(topAnim);
        }
        loginButton.setAnimation(bottomAnim);
        emailInput.setAnimation(rightAnim);
        passwordInput.setAnimation(leftAnim);
        admissionNumberInput.setAnimation(leftAnim);

        // Set click listener for login button
        loginButton.setOnClickListener(v -> loginStudent());
    }


    private void loginStudent() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String admissionNumber = admissionNumberInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || admissionNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            // ✅ Fetch student details only after login succeeds
                            fetchStudentData(admissionNumber, uid);
                        }
                    } else {
                        Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchStudentData(String admissionNumber, String uid) {
        DocumentReference studentRef = db.collection("Students").document(admissionNumber);

        studentRef.get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String storedUid = document.getString("uid");
                        String studentName = document.getString("name");

                        if (storedUid != null && storedUid.equals(uid)) {
                            Toast.makeText(this, "Welcome " + studentName, Toast.LENGTH_SHORT).show();

                            // Redirect to Student Dashboard
                            Intent intent = new Intent(this, studentdashboard.class);
                            intent.putExtra("studentName", studentName);
                            intent.putExtra("admissionNumber", admissionNumber);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Authentication Failed! UID mismatch.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Student record not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }}
