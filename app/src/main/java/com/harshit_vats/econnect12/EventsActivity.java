package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private FirebaseFirestore db;
    private EventsAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // Initialize views
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventList = new ArrayList<>();
        eventAdapter = new EventsAdapter(eventList);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventAdapter);

        // Fetch events from Firestore
        fetchEventsFromFirestore();
    }

    private void fetchEventsFromFirestore() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        eventList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
                    } else {
                        Toast.makeText(EventsActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
