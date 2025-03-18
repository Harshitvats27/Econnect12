package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentHomeworkActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeworkAdapter homeworkAdapter;
    private List<HomeworkModel> homeworkList;
    private FirebaseFirestore db;
    private String studentClass, studentSection, studentRoll;
    private TextView selectedDateText;
    private String selectedDate;
    private FirebaseAuth mAuth;
    private TextView studentName, studentAdmissionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homework);

        recyclerView = findViewById(R.id.recycler_homework);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectedDateText = findViewById(R.id.text_selected_date);
        CalendarView calendarView = findViewById(R.id.calendarView);

        homeworkList = new ArrayList<>();
        homeworkAdapter = new HomeworkAdapter(homeworkList);
        recyclerView.setAdapter(homeworkAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Set default date to today in YYYY-MM-DD format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date());
        selectedDateText.setText("Selected Date: " + selectedDate);

        fetchStudentDetails();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            selectedDateText.setText("Selected Date: " + selectedDate);

            if (studentClass != null && studentSection != null) {
                fetchHomeworkForDate();
            }
        });
    }

    private void fetchStudentDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userUid = user.getUid();
        Log.d("StudentFetch", "Fetching details for UID: " + userUid);

        CollectionReference studentsRef = db.collection("Students");

        studentsRef.whereEqualTo("uid", userUid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        studentClass = document.getString("class");
                        studentSection = document.getString("section");
                        studentRoll = document.getId();

                        Log.d("Student Details", "Class: " + studentClass + ", Section: " + studentSection);

                        if (studentClass != null && studentSection != null) {
                            fetchHomeworkForDate();
                        }
                    } else {
                        Log.e("Firestore", "Student details not found for UID: " + userUid);
                        Toast.makeText(this, "Student details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching student details", e);
                    Toast.makeText(this, "Error loading student details", Toast.LENGTH_SHORT).show();
                });
    }

    public void fetchHomeworkForDate() {
        if (studentClass == null || studentSection == null) {
            Toast.makeText(this, "Error: Student details not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("FirestoreDebug", "Fetching homework for Date: " + selectedDate +
                ", Class: " + studentClass + ", Section: " + studentSection);

        db.collection("classes").document(studentClass)
                .collection("sections").document(studentSection)
                .collection("homework")
                .whereEqualTo("date", selectedDate) // Ensure this field exists in Firestore
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    homeworkList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d("FirestoreDebug", "No homework found for selected date.");
                        Toast.makeText(this, "No homework assigned for this date!", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Log.d("FirestoreDebug", "Matching Homework Found: " + document.getId() + ", Data: " + document.getData());
                            HomeworkModel homework = document.toObject(HomeworkModel.class);
                            homeworkList.add(homework);
                        }
                    }

                    homeworkAdapter.updateHomeworkList(homeworkList);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Error fetching homework", e);
                    Toast.makeText(this, "Failed to fetch homework. Try again!", Toast.LENGTH_SHORT).show();
                });
    }


}
