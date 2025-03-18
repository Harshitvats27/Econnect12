package com.harshit_vats.econnect12;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FacultyLoginActivity extends AppCompatActivity {

    private EditText facultyEmail, facultyPassword;
    private Button loginFacultyBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        facultyEmail = findViewById(R.id.faculty_email);
        facultyPassword = findViewById(R.id.faculty_password);
        loginFacultyBtn = findViewById(R.id.login_faculty_btn);

        // Login Button Click Listener
        loginFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFaculty();
            }
        });
    }

    private void loginFaculty() {
        String email = facultyEmail.getText().toString().trim();
        String password = facultyPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(email)) {
            facultyEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            facultyPassword.setError("Password is required");
            return;
        }

        // Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(FacultyLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FacultyLoginActivity.this, FacultyDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(FacultyLoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
