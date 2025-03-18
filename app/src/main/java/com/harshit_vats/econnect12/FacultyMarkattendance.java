//package com.example.econnect12;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class FacultyMarkattendance extends AppCompatActivity {
//
//    private Spinner classSpinner, sectionSpinner;
//    private RecyclerView recyclerView;
//    private Button submitAttendanceButton, selectDateButton;
//    private TextView selectedDateText;
//    private AttendanceAdapter adapter;
//    private List<Student> studentList = new ArrayList<>();
//    private FirebaseFirestore db;
//    private String selectedClass = "1";  // Default class
//    private String selectedSection = "A"; // Default section
//    private String selectedDate = "2024-03-19";  // Default date (change dynamically)
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_faculty_markattendance);
//
//        // Initialize Views
//        classSpinner = findViewById(R.id.classSpinner);
//        sectionSpinner = findViewById(R.id.sectionSpinner);
//        recyclerView = findViewById(R.id.recyclerView);
//        submitAttendanceButton = findViewById(R.id.submit_attendance_btn);
//        selectDateButton = findViewById(R.id.select_date_btn);
//        selectedDateText = findViewById(R.id.selected_date_tv);
//        db = FirebaseFirestore.getInstance();
//
//        // Set up RecyclerView
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new AttendanceAdapter(studentList);
//        recyclerView.setAdapter(adapter);
//
//        // Set up class spinner
//        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(
//                this, R.array.classes, android.R.layout.simple_spinner_item);
//        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        classSpinner.setAdapter(classAdapter);
//
//        // Set up section spinner
//        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.createFromResource(
//                this, R.array.sections, android.R.layout.simple_spinner_item);
//        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sectionSpinner.setAdapter(sectionAdapter);
//
//        // Spinner selection listeners
//        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedClass = parent.getItemAtPosition(position).toString();
//                fetchStudentsFromFirestore();  // Fetch students when class changes
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//
//        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedSection = parent.getItemAtPosition(position).toString();
//                fetchStudentsFromFirestore();  // Fetch students when section changes
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//
//        // Date Picker Button
//        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
//
//        // Submit Attendance Button
//        submitAttendanceButton.setOnClickListener(v -> submitAttendance());
//    }
//
//    private void fetchStudentsFromFirestore() {
//        CollectionReference studentRef = db.collection("Students")
//                .document(selectedClass)
//                .collection(selectedSection);
//
//        studentRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult() != null) {
//                studentList.clear();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    String regNum = document.getId();
//                    String name = document.getString("name");
//                    String email = document.getString("email");
//                    studentList.add(new Student(regNum, name, email, false));
//                }
//                adapter.notifyDataSetChanged();
//            } else {
//                Toast.makeText(this, "Failed to load students", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void showDatePickerDialog() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                (view, year1, month1, dayOfMonth) -> {
//                    selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
//                    selectedDateText.setText("Date: " + selectedDate);
//                }, year, month, day);
//        datePickerDialog.show();
//    }
//
//    private void submitAttendance() {
//        List<Student> updatedStudents = adapter.getUpdatedStudentList();
//        Map<String, Object> attendanceData = new HashMap<>();
//
//        for (Student student : updatedStudents) {
//            attendanceData.put(student.getRegistrationNumber(), student.isPresent());
//        }
//
//        db.collection("attendance").document(selectedClass)
//                .collection(selectedSection)
//                .document(selectedDate)
//                .set(attendanceData)
//                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Attendance Updated", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show());
//    }
//}
