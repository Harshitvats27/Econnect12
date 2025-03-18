package com.harshit_vats.econnect12;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StudentViewAttendanceActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView selectedDateText, attendanceStatusText;

    private String studentId;
    private String studentClass;
    private String studentSection;

    private FirebaseFirestore db;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_attendance);

        // Initialize UI components
        calendarView = findViewById(R.id.calendarView);
        selectedDateText = findViewById(R.id.selectedDateText);
        attendanceStatusText = findViewById(R.id.attendanceStatusText);

        // Initialize Firestore and Date Format
        db = FirebaseFirestore.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Get data from Intent
        Intent intent = getIntent();
        studentId = intent.getStringExtra("studentId");
        studentClass = intent.getStringExtra("studentClass");
        studentSection = intent.getStringExtra("studentSection");

        if (studentId == null || studentClass == null || studentSection == null) {
            Toast.makeText(this, "Missing student info.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Show attendance for today's date by default
        Calendar today = Calendar.getInstance();
        String todayDate = sdf.format(today.getTime());
        selectedDateText.setText("Selected Date: " + todayDate);
        fetchAttendance(todayDate);

        // Set listener for date change
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            String date = sdf.format(selected.getTime());

            selectedDateText.setText("Selected Date: " + date);
            fetchAttendance(date);
        });
    }

    private void fetchAttendance(String selectedDate) {
        DocumentReference docRef = db.collection("attendance")
                .document(selectedDate)
                .collection(studentClass)
                .document(studentSection)
                .collection("Students")
                .document(studentId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Boolean present = documentSnapshot.getBoolean("present");
                if (present != null && present) {
                    attendanceStatusText.setText("Attendance Status: ✅ Present");
                } else {
                    attendanceStatusText.setText("Attendance Status: ❌ Absent");
                }
            } else {
                attendanceStatusText.setText("Attendance Status: 🚫 No record found");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load attendance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
