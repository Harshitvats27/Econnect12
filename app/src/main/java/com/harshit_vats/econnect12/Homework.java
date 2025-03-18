package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Homework extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSection;
    private EditText editHomework;
    private Button btnPostHomework;
    private TextView facultyName, facultyDepartment;
    private FirebaseFirestore db;
    private String facultyId, department;
    private Map<String, List<String>> assignedClasses = new HashMap<>();
    private ArrayAdapter<String> classAdapter, sectionAdapter;
    private List<String> classList = new ArrayList<>();
    private List<String> sectionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        // Initialize UI elements
        spinnerClass = findViewById(R.id.spinner_class);
        spinnerSection = findViewById(R.id.spinner_section);
        editHomework = findViewById(R.id.edit_homework);
        btnPostHomework = findViewById(R.id.btn_post_homework);
        facultyName = findViewById(R.id.faculty_name);
        facultyDepartment = findViewById(R.id.faculty_department);

        db = FirebaseFirestore.getInstance();
        facultyId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load faculty details and assigned classes
        loadFacultyDetails();

        btnPostHomework.setOnClickListener(view -> postHomework());

        // Handle class selection change to update sections
        spinnerClass.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateSections(classList.get(position));
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void loadFacultyDetails() {
        db.collection("faculties")
                .whereEqualTo("uid", facultyId) // Find faculty document where UID matches
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // Get faculty name & department
                        String name = document.getString("faculty_name");
                        department = document.getString("faculty_department");

                        // Update UI
                        facultyName.setText(name);
                        facultyDepartment.setText(department);

                        // Load assigned classes
                        loadAssignedClasses(document);
                    } else {
                        Toast.makeText(this, "Faculty details not found!", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Faculty details not found for UID: " + facultyId);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching faculty details", e));
    }

    private void loadAssignedClasses(DocumentSnapshot document) {
        Map<String, Object> assigned = (Map<String, Object>) document.get("assigned_classes");
        if (assigned != null) {
            for (Map.Entry<String, Object> entry : assigned.entrySet()) {
                String className = entry.getKey();
                List<String> sections = (List<String>) entry.getValue();
                assignedClasses.put(className, sections);
                classList.add(className);
            }

            classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classList);
            spinnerClass.setAdapter(classAdapter);

            // Load sections for the first class
            if (!classList.isEmpty()) {
                updateSections(classList.get(0));
            }
        }
    }

    private void updateSections(String selectedClass) {
        sectionList.clear();
        if (assignedClasses.containsKey(selectedClass)) {
            sectionList.addAll(assignedClasses.get(selectedClass));
        }
        sectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionList);
        spinnerSection.setAdapter(sectionAdapter);
    }

    private void postHomework() {
        String classSelected = spinnerClass.getSelectedItem() != null ? spinnerClass.getSelectedItem().toString() : "";
        String sectionSelected = spinnerSection.getSelectedItem() != null ? spinnerSection.getSelectedItem().toString() : "";
        String homeworkText = editHomework.getText().toString().trim();

        if (homeworkText.isEmpty()) {
            Toast.makeText(this, "Enter homework details", Toast.LENGTH_SHORT).show();
            return;
        }
        if (classSelected.isEmpty() || sectionSelected.isEmpty()) {
            Toast.makeText(this, "Select Class and Section", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Map<String, Object> homeworkData = new HashMap<>();
        homeworkData.put("description", homeworkText);
        homeworkData.put("date", date);
        homeworkData.put("faculty_id", facultyId);
        homeworkData.put("faculty_name", facultyName.getText().toString());
        homeworkData.put("subject", department);

        db.collection("classes").document(classSelected)
                .collection("sections").document(sectionSelected)
                .collection("homework").add(homeworkData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Homework Posted!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to Post", Toast.LENGTH_SHORT).show());
    }
}
