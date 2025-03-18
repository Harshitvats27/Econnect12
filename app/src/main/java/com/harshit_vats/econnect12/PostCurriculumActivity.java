package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostCurriculumActivity extends AppCompatActivity {

    private EditText curriculumClass, curriculumDetails;
    private Button postCurriculumButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_curriculum);

        curriculumClass = findViewById(R.id.curriculumClass);
        curriculumDetails = findViewById(R.id.curriculumDetails);
        postCurriculumButton = findViewById(R.id.postCurriculumButton);
        db = FirebaseFirestore.getInstance();

        postCurriculumButton.setOnClickListener(v -> postCurriculum());
    }

    private void postCurriculum() {
        // Get curriculum details
        String className = curriculumClass.getText().toString().trim();
        String details = curriculumDetails.getText().toString().trim();

        if (className.isEmpty() || details.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the data to be posted
        Map<String, Object> curriculum = new HashMap<>();
        curriculum.put("curriculum_details", details);

        // Save the curriculum for the selected class
        db.collection("curriculum")
                .document( className) // Class is identified as class_1, class_2, etc.
                .set(curriculum)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PostCurriculumActivity.this, "Curriculum posted successfully", Toast.LENGTH_SHORT).show();
                    curriculumClass.setText("");
                    curriculumDetails.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PostCurriculumActivity.this, "Failed to post curriculum", Toast.LENGTH_SHORT).show();
                });
    }
}
