package com.harshit_vats.econnect12;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class AdminAttendanceActivity extends AppCompatActivity {

    private Spinner classSpinner, sectionSpinner;
    private Button selectDateButton, submitAttendanceButton;
    private TextView selectedDateText;
    private RecyclerView studentsRecyclerView;
    private FirebaseFirestore db;
    private StudentAdapter studentAdapter;
    private ArrayList<StudentModel> studentList = new ArrayList<>();
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance);

        db = FirebaseFirestore.getInstance();

        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectedDateText = findViewById(R.id.selectedDateText);
        studentsRecyclerView = findViewById(R.id.studentsRecyclerView);
        submitAttendanceButton = findViewById(R.id.submitAttendanceButton);

        studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(studentList);
        studentsRecyclerView.setAdapter(studentAdapter);

        loadClassSpinner(); // Load classes from Firestore

        selectDateButton.setOnClickListener(v -> openDatePicker());
        submitAttendanceButton.setOnClickListener(v -> submitAttendance());
    }

    private void loadClassSpinner() {
        db.collection("Students")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashSet<String> classSet = new HashSet<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String classValue = document.getString("class");
                        if (classValue != null) classSet.add(classValue);
                    }

                    ArrayList<String> classList = new ArrayList<>(classSet);
                    Collections.sort(classList);
                    ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, classList);
                    classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    classSpinner.setAdapter(classAdapter);

                    classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedClass = classList.get(position);
                            loadSectionSpinner(selectedClass);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load classes", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "loadClassSpinner", e);
                });
    }

    private void loadSectionSpinner(String selectedClass) {
        db.collection("Students")
                .whereEqualTo("class", selectedClass)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashSet<String> sectionSet = new HashSet<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String sectionValue = document.getString("section");
                        if (sectionValue != null) sectionSet.add(sectionValue);
                    }

                    ArrayList<String> sectionList = new ArrayList<>(sectionSet);
                    Collections.sort(sectionList);

                    ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, sectionList);
                    sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sectionSpinner.setAdapter(sectionAdapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load sections", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "loadSectionSpinner", e);
                });
    }

    private void openDatePicker() {
        if (classSpinner.getSelectedItem() == null || sectionSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select both class and section first", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                    selectedDateText.setText("Selected Date: " + selectedDate);
                    fetchStudents();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void fetchStudents() {
        if (classSpinner.getSelectedItem() == null || sectionSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a class and section", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedClass = classSpinner.getSelectedItem().toString().trim();
        String selectedSection = sectionSpinner.getSelectedItem().toString().trim();

        if (selectedClass.isEmpty() || selectedSection.isEmpty()) {
            Toast.makeText(this, "Invalid class or section selection", Toast.LENGTH_SHORT).show();
            return;
        }

        studentList.clear();
        studentAdapter.notifyDataSetChanged();

        db.collection("Students")
                .whereEqualTo("class", selectedClass)
                .whereEqualTo("section", selectedSection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String studentId = document.getId();
                            String studentName = document.getString("name");
                            String admissionNumber = document.getString("admissionNumber");
                            String rollNumber = String.valueOf(document.get("roll_number"));

                            if (studentId != null && studentName != null) {
                                studentList.add(new StudentModel(
                                        studentId,
                                        studentName,
                                        admissionNumber != null ? admissionNumber : "N/A",
                                        rollNumber != null ? rollNumber : "N/A"
                                ));
                            }
                        }
                        studentAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Failed to load students", task.getException());
                        Toast.makeText(this, "Failed to load students", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void submitAttendance() {
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (classSpinner.getSelectedItem() == null || sectionSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a class and section", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedClass = classSpinner.getSelectedItem().toString().trim();
        String selectedSection = sectionSpinner.getSelectedItem().toString().trim();

        if (selectedClass.isEmpty() || selectedSection.isEmpty()) {
            Toast.makeText(this, "Invalid class or section selection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (studentList.isEmpty()) {
            Toast.makeText(this, "No students found for this class and section", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference attendanceRef = db.collection("attendance")
                .document(selectedDate)
                .collection(selectedClass)
                .document(selectedSection)
                .collection("Students");

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            for (StudentModel student : studentList) {
                if (student == null || student.getId() == null) {
                    Log.e("AttendanceError", "Skipping student: null ID!");
                    continue;
                }

                Map<String, Object> attendanceData = new HashMap<>();
                attendanceData.put("present", student.isPresent());
                attendanceData.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()));

                DocumentReference studentDoc = attendanceRef.document(student.getId());
                transaction.set(studentDoc, attendanceData);
            }
            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Attendance recorded successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Failed to record attendance", e);
            Toast.makeText(this, "Failed to record attendance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}
