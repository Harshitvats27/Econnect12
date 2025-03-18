package com.harshit_vats.econnect12;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.SharedPreferences;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView adminNameText, adminRoleText;
    private ImageView profile;

    private CardView cardRegisterStudent, cardRegisterFaculty, cardPostEvent, cardPostNotice, cardPostCurriculum, cardMarkAttendance; // ✅ Removed trailing comma

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        adminNameText = findViewById(R.id.adminName);
        adminRoleText = findViewById(R.id.adminEmail);
        profile = findViewById(R.id.adminImage);

        // Link CardViews by ID
        cardRegisterStudent = findViewById(R.id.card_registerStudent);
        cardRegisterFaculty = findViewById(R.id.card_Register_Faculty);
        cardPostEvent = findViewById(R.id.card_Post_Event);
        cardPostNotice = findViewById(R.id.card_postnotice);
        cardPostCurriculum = findViewById(R.id.card_postcurriculum);
        cardMarkAttendance= findViewById(R.id.card_MarkAttendance);


        cardRegisterStudent.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterStudents.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_annimation, R.anim.left_annimation);
        });

        cardRegisterFaculty.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterFaculty.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_annimation, R.anim.left_annimation);
        });

        cardPostEvent.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventPostingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_annimation, R.anim.left_annimation);
        });

        cardPostNotice.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoticePostingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_annimation, R.anim.left_annimation);
        });

        cardPostCurriculum.setOnClickListener(v -> {
            Intent intent = new Intent(this, PostCurriculumActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_annimation, R.anim.left_annimation);
        });

        cardMarkAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminAttendanceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_annimation, R.anim.left_annimation);
        });


        // Enable Firestore network
        db.enableNetwork()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Network enabled"))
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to enable network", e));

        fetchAdminDetails();
    }

    private void fetchAdminDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        String adminId = sharedPreferences.getString("adminId", null);

        if (adminId != null) {
            db.collection("Admins").document(adminId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String role = documentSnapshot.getString("role");

                            adminNameText.setText("Welcome! " + name);
                            adminRoleText.setText("Role: " + role);
                        } else {
                            showToast("Admin details not found");
                        }
                    })
                    .addOnFailureListener(e -> {
                        showToast("Error fetching admin details");
                        Log.e("Firestore", "Error fetching admin details", e);
                    });
        } else {
            showToast("Admin not logged in");
        }
    }

    private void logoutAdmin() {
        auth.signOut();
        showToast("Logged out successfully!");
        startActivity(new Intent(this, adminlogin.class));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
