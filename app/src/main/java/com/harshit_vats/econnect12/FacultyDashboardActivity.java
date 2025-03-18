package com.harshit_vats.econnect12;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FacultyDashboardActivity extends AppCompatActivity {

    private ImageView facultyImage;
    private TextView facultyName, facultyDesignation;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    // Use CardView for consistency
    private CardView cardAttendance, cardNotices, cardCurriculum, cardHomework, cardSchedule, cardEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Profile Section
        facultyImage = findViewById(R.id.facultyImage);
        facultyName = findViewById(R.id.facultyName);
        facultyDesignation = findViewById(R.id.facultyDesignation);

        // Grid Options
        cardAttendance = findViewById(R.id.card_attendance);
        cardNotices = findViewById(R.id.card_notice);
        cardCurriculum = findViewById(R.id.card_curriculum);
        cardHomework = findViewById(R.id.card_homework);
        cardSchedule = findViewById(R.id.card_schedule);
        cardEvents = findViewById(R.id.card_postmarks);

        // Load faculty details
        loadFacultyDetails();

        // Set Click Listeners
//        cardSchedule.setOnClickListener(view -> openActivity(ScheduleActivity.class));
        cardCurriculum.setOnClickListener(view -> openActivity(ViewCurriculumActivity.class));
//        cardAttendance.setOnClickListener(view -> openActivity(FacultyMarkAttendance.class));
        cardNotices.setOnClickListener(view -> openActivity(NoticeDisplayActivity.class));
//        cardHomework.setOnClickListener(view -> openActivity(HomeworkActivity.class));
        cardEvents.setOnClickListener(view -> openActivity(EventsActivity.class));
    }

    private void loadFacultyDetails() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get logged-in faculty UID

        db.collection("faculties")
                .whereEqualTo("uid", userUid) // Find faculty document where UID matches
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Get the first matching document (since UID should be unique)
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // Extract faculty details
                        String name = document.getString("faculty_name");
                        String department = document.getString("faculty_department");

                        // Update UI
                        facultyName.setText(name);
                        facultyDesignation.setText(department);
                        facultyImage.setImageResource(R.drawable.ic_profile);
                    } else {
                        Log.e("Firestore", "Faculty details not found for UID: " + userUid);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching faculty details", e));
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(FacultyDashboardActivity.this, activityClass);
        startActivity(intent);
    }
}