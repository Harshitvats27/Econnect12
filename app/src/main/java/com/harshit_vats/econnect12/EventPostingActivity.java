package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventPostingActivity extends AppCompatActivity {

    private EditText eventName, eventDescription, eventDate, eventLocation, eventTime,eventtype;
    private Button postEventButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_posting);

        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventDate = findViewById(R.id.eventDate); // Format: YYYY-MM-DD
        eventLocation = findViewById(R.id.eventLocation);
        eventTime = findViewById(R.id.eventTime);
        postEventButton = findViewById(R.id.postEventButton);
        eventtype=findViewById(R.id.eventtype);
        db = FirebaseFirestore.getInstance();

        postEventButton.setOnClickListener(v -> postEvent());
    }

    private void postEvent() {
        // Get event details
        String name = eventName.getText().toString();
        String Eventtype=eventtype.getText().toString();
        String description = eventDescription.getText().toString();
        String date = eventDate.getText().toString();
        String location = eventLocation.getText().toString();
        String time = eventTime.getText().toString();

        if (name.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data for Firestore
        Map<String, Object> event = new HashMap<>();
        event.put("event_name", name);
        event.put("description", description);
        event.put("event_date", date);
        event.put("location", location);
        event.put("event_time", time);
        event.put("posted_by", "Faculty/Admin"); // You can replace this with dynamic user info
        event.put("category",Eventtype); // Example category, can be dynamic

        // Add event to Firestore
        db.collection("events")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(EventPostingActivity.this, "Event posted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(EventPostingActivity.this, "Failed to post event", Toast.LENGTH_SHORT).show());
    }
}
