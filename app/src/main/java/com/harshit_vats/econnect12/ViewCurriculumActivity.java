package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ViewCurriculumActivity extends AppCompatActivity {

    private TextView curriculumTextView;
    private RecyclerView classRecyclerView;
    private FirebaseFirestore db;
    private List<String> classList;
    private ClassAdapter classAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_curriculum);

        curriculumTextView = findViewById(R.id.curriculumTextView);
        classRecyclerView = findViewById(R.id.classRecyclerView);
        db = FirebaseFirestore.getInstance();
        classList = new ArrayList<>();

        // Setup RecyclerView
        classRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new ClassAdapter(classList, this::fetchCurriculumForClass);
        classRecyclerView.setAdapter(classAdapter);


        // Fetch classes from Firestore
        fetchClassesFromFirestore();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void fetchClassesFromFirestore() {
        db.collection("curriculum")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        classList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String className = document.getId(); // Class ID (class_1, class_2)
                            classList.add(className);
                        }
                        classAdapter.notifyDataSetChanged(); // Update the RecyclerView
                    } else {
                        Toast.makeText(ViewCurriculumActivity.this, "Failed to load classes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchCurriculumForClass(String className) {
        db.collection("curriculum")
                .document(className)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String curriculum = task.getResult().getString("curriculum_details");
                        if (curriculum != null) {
                            curriculumTextView.setText(curriculum); // Display curriculum
                            curriculumTextView.post(() -> curriculumTextView.getParent().requestChildFocus(curriculumTextView, curriculumTextView));
                        } else {
                            curriculumTextView.setText("No curriculum available for this class.");
                        }
                    } else {
                        Toast.makeText(ViewCurriculumActivity.this, "Failed to load curriculum", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
