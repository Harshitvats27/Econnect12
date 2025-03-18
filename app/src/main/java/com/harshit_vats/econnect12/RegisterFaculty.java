package com.harshit_vats.econnect12;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import  com.harshit_vats.econnect12.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterFaculty extends AppCompatActivity {
    private EditText facultyId, facultyName, facultyEmail, facultyPassword, facultyDepartment;
    private LinearLayout classCheckboxContainer, sectionCheckboxContainer;
    private Button registerFacultyBtn;
    private FirebaseFirestore db;

    private String[] classes = {"1", "2", "3", "4", "5"};  // Example classes
    private String[] sections = {"A", "B", "C", "D"};  // Sections for each class

    private HashMap<String, ArrayList<String>> classSectionsMap = new HashMap<>(); // Stores selected class-section mapping
    private HashMap<String, LinearLayout> sectionLayouts = new HashMap<>(); // Keeps track of section layouts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_faculty);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // UI Elements
        facultyId = findViewById(R.id.faculty_id);
        facultyName = findViewById(R.id.faculty_name);
        facultyEmail = findViewById(R.id.faculty_email);
        facultyPassword = findViewById(R.id.faculty_password);
        facultyDepartment = findViewById(R.id.faculty_department);
        classCheckboxContainer = findViewById(R.id.classCheckboxContainer);
        sectionCheckboxContainer = findViewById(R.id.sectionCheckboxContainer);
        registerFacultyBtn = findViewById(R.id.register_faculty_btn);

        // Dynamically add class checkboxes
        addClassCheckboxes();

        // Register faculty button click
        registerFacultyBtn.setOnClickListener(v -> registerFaculty());
    }

    private void addClassCheckboxes() {
        for (String className : classes) {
            CheckBox classCheckbox = new CheckBox(this);
            classCheckbox.setText("Class " + className);
            classCheckboxContainer.addView(classCheckbox);

            // Create a layout for sections (Initially hidden)
            LinearLayout sectionLayout = new LinearLayout(this);
            sectionLayout.setOrientation(LinearLayout.VERTICAL);
            sectionLayout.setVisibility(View.GONE);
            sectionLayouts.put(className, sectionLayout);
            sectionCheckboxContainer.addView(sectionLayout);

            // Handle class checkbox selection
            classCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    sectionLayout.setVisibility(View.VISIBLE);
                    addSectionCheckboxes(className, sectionLayout);
                } else {
                    sectionLayout.setVisibility(View.GONE);
                    classSectionsMap.remove(className);
                }
            });
        }
    }

    private void addSectionCheckboxes(String className, LinearLayout sectionLayout) {
        sectionLayout.removeAllViews(); // Clear previous checkboxes
        ArrayList<String> selectedSections = new ArrayList<>();

        for (String section : sections) {
            CheckBox sectionCheckbox = new CheckBox(this);
            sectionCheckbox.setText("Section " + section);
            sectionLayout.addView(sectionCheckbox);

            // Handle section checkbox selection
            sectionCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedSections.add(section);
                } else {
                    selectedSections.remove(section);
                }
                classSectionsMap.put(className, selectedSections);
            });
        }
    }

    private void registerFaculty() {
        // Retrieve values from the input fields
        String id = facultyId.getText().toString().trim();  // User input for faculty ID
        String name = facultyName.getText().toString().trim();
        String email = facultyEmail.getText().toString().trim();
        String password = facultyPassword.getText().toString().trim();
        String department = facultyDepartment.getText().toString().trim();

        // Check if any fields are empty
        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || department.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if class and section are selected
        if (classSectionsMap.isEmpty()) {
            Toast.makeText(this, "Select at least one class and section", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register faculty using Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userUid = user.getUid();  // Get UID generated by Firebase Authentication

                        // Prepare faculty data with the user-provided ID and Firebase UID
                        Map<String, Object> facultyData = new HashMap<>();
                        facultyData.put("uid", userUid);  // Store the Firebase UID in Firestore
                        facultyData.put("faculty_name", name);
                        facultyData.put("faculty_email", email);
                        facultyData.put("faculty_department", department);
                        facultyData.put("assigned_classes", classSectionsMap);

                        // Store the faculty data in Firestore using the user-provided ID
                        db.collection("faculties").document(id)  // Use the user-provided ID as document ID
                                .set(facultyData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(RegisterFaculty.this, "Faculty Registered", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(RegisterFaculty.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        // Handle failure in account creation
                        Toast.makeText(RegisterFaculty.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

