package com.harshit_vats.econnect12;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class studentdashboard extends AppCompatActivity {

    private TextView studentName, studentClass;
    private CardView cardSchedule, cardAttendance, cardHomework, cardNotice, cardCurriculum, cardPostMarks, cardEvents;
    private Button btnLogout;

    // Stored student info for reuse
    private String studentId, studentClassValue, studentSectionValue;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentdashboard);

        // Initialize UI components
        studentName = findViewById(R.id.studentName);
        studentClass = findViewById(R.id.studentClass);

        cardSchedule = findViewById(R.id.student_card_schedule);
        cardAttendance = findViewById(R.id.student_card_attendance);
        cardHomework = findViewById(R.id.student_card_homework);
        cardNotice = findViewById(R.id.student_card_notice);
        cardCurriculum = findViewById(R.id.student_card_curriculum);
        cardPostMarks = findViewById(R.id.student_card_postmarks);
        btnLogout = findViewById(R.id.student_btn_logout);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Set Click Listeners
        // cardSchedule.setOnClickListener(v -> startActivity(new Intent(this, StudentScheduleActivity.class)));
        cardHomework.setOnClickListener(v -> startActivity(new Intent(this, StudentHomeworkActivity.class)));
        cardNotice.setOnClickListener(v -> startActivity(new Intent(this, NoticeDisplayActivity.class)));
        cardCurriculum.setOnClickListener(v -> startActivity(new Intent(this, ViewCurriculumActivity.class)));
        // cardPostMarks.setOnClickListener(v -> startActivity(new Intent(this, PostMarksActivity.class)));

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, studentlogin.class));
            finish();
        });

        cardAttendance.setOnClickListener(v -> {
            if (studentId != null && studentClassValue != null && studentSectionValue != null) {
                Intent intent = new Intent(this, StudentViewAttendanceActivity.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("studentClass", studentClassValue);
                intent.putExtra("studentSection", studentSectionValue);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Student information not loaded yet.", Toast.LENGTH_SHORT).show();
            }
        });

        // Load student details once
        loadStudentDetails();
    }

    private void loadStudentDetails() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, studentlogin.class));
            finish();
            return;
        }

        String userUid = user.getUid();
        CollectionReference studentsRef = db.collection("Students");

        studentsRef.whereEqualTo("uid", userUid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // Save student info for reuse
                        studentId = document.getId();
                        studentClassValue = document.getString("class");
                        studentSectionValue = document.getString("section");
                        String name = document.getString("name");

                        // Update UI
                        studentName.setText(name);
                        studentClass.setText("Class: " + studentClassValue + " " + studentSectionValue);
                    } else {
                        Toast.makeText(this, "Student details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching student details", e);
                    Toast.makeText(this, "Error loading student details", Toast.LENGTH_SHORT).show();
                });
    }
}
