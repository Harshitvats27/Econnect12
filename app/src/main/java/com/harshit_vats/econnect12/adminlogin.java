package com.harshit_vats.econnect12;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class adminlogin extends AppCompatActivity {
 private TextView heading;
 private EditText email, password;
 private Button login;
 ImageView logo;
    private FirebaseFirestore db;
    Animation topAnim, bottomAnim , leftAnim, rightAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adminlogin);
        logo=findViewById(R.id.logo);
        heading=findViewById(R.id.heading);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.loginBtn);
        db = FirebaseFirestore.getInstance();
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        rightAnim = AnimationUtils.loadAnimation(this,R.anim.right_annimation);
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.left_annimation);

        heading.setAnimation(topAnim);
        login.setAnimation(bottomAnim);
        email.setAnimation(rightAnim);
        password.setAnimation(leftAnim);

        login.setOnClickListener(v -> loginAdmin());



    }

    private void loginAdmin() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        // Basic validation
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Admins")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean isAdminFound = false;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String dbEmail = document.getString("email");
                        String dbPassword = document.getString("password");
                        String adminId = document.getId();  // Retrieve adminId (document ID)

                        if (dbEmail != null && dbPassword != null &&
                                dbEmail.equals(emailText) && dbPassword.equals(passwordText)) {

                            isAdminFound = true;

                            // Save the adminId in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("adminId", adminId);  // Save adminId
                            editor.apply();

                            Toast.makeText(adminlogin.this, "Admin Login Successful!", Toast.LENGTH_SHORT).show();

                            // Redirect to Admin Dashboard
                            Intent intent = new Intent(adminlogin.this, AdminDashboardActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    }

                    if (!isAdminFound) {
                        Toast.makeText(adminlogin.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(adminlogin.this, "Database Error!", Toast.LENGTH_SHORT).show());
    }

}
