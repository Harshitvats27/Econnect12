package com.harshit_vats.econnect12;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Locale;

public class NoticeDisplayActivity extends AppCompatActivity {

    private RecyclerView upcomingRecyclerView, completedRecyclerView;
    private NoticeAdapter upcomingAdapter, completedAdapter;
    private FirebaseFirestore db;

    private ImageView upcomingArrow, completedArrow;
    private CardView upcomingSection, completedSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_display);

        db = FirebaseFirestore.getInstance();

        upcomingRecyclerView = findViewById(R.id.upcomingRecyclerView);
        completedRecyclerView = findViewById(R.id.completedRecyclerView);
        upcomingArrow = findViewById(R.id.upcomingArrow);
        completedArrow = findViewById(R.id.completedArrow);
        upcomingSection = findViewById(R.id.upcomingSection);
        completedSection = findViewById(R.id.completedSection);

        // Initialize the adapters with empty lists
        upcomingAdapter = new NoticeAdapter(new ArrayList<>(), true);
        completedAdapter = new NoticeAdapter(new ArrayList<>(), false);

        // Set LayoutManager and Adapters for RecyclerViews
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        completedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingRecyclerView.setAdapter(upcomingAdapter);
        completedRecyclerView.setAdapter(completedAdapter);

        setupToggleListeners(); // Set up the toggle visibility listeners
        fetchNoticesFromFirestore(); // Fetch notices from Firestore
    }

    private void setupToggleListeners() {
        // Toggle visibility for upcoming notices section
        upcomingSection.setOnClickListener(v -> {
            Log.d("NoticeDisplayActivity", "Upcoming section clicked");
            toggleSectionVisibility(upcomingRecyclerView, upcomingArrow);
        });

        // Toggle visibility for completed notices section
        completedSection.setOnClickListener(v -> {
            Log.d("NoticeDisplayActivity", "Completed section clicked");
            toggleSectionVisibility(completedRecyclerView, completedArrow);
        });
    }

    private void toggleSectionVisibility(RecyclerView recyclerView, ImageView arrow) {
        boolean isVisible = recyclerView.getVisibility() == View.VISIBLE;
        if (isVisible) {
            recyclerView.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_expand_more); // Change to the expand icon
            Log.d("NoticeDisplayActivity", "Hiding RecyclerView");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.ic_expand_less); // Change to the collapse icon
            Log.d("NoticeDisplayActivity", "Showing RecyclerView");
        }
    }

    private void fetchNoticesFromFirestore() {
        CollectionReference noticesRef = db.collection("notices");

        noticesRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Notice> upcomingNotices = new ArrayList<>();
                        List<Notice> completedNotices = new ArrayList<>();
                        long nowMillis = System.currentTimeMillis();
                        Date now = new Date(nowMillis);

                        // Loop through each document returned by Firestore
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String noticeTitle = document.getString("notice_title");
                            String noticeDescription = document.getString("notice_description");
                            String postedBy = document.getString("posted_by");

                            // Fetch the notice date (as a Timestamp)
                            Object noticeDateObj = document.get("notice_date");

                            Date noticeDate = null;
                            if (noticeDateObj instanceof Timestamp) {
                                // If it's a Timestamp, convert to Date
                                noticeDate = ((Timestamp) noticeDateObj).toDate();
                            } else if (noticeDateObj instanceof Date) {
                                // If it's already a Date, use it directly
                                noticeDate = (Date) noticeDateObj;
                            } else if (noticeDateObj instanceof String) {
                                // If it's a String, try to parse it (if necessary)
                                try {
                                    noticeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse((String) noticeDateObj);
                                } catch (ParseException e) {
                                    Log.d("Firestore", "Error parsing date string: " + e.getMessage());
                                }
                            }

                            // Create a Notice object
                            Notice notice = new Notice(noticeTitle, noticeDescription, noticeDate, postedBy);

                            // Separate notices into upcoming and completed lists based on the event date
                            if (noticeDate != null) {
                                if (noticeDate.after(now)) {
                                    upcomingNotices.add(notice); // Upcoming notice if eventDate is in the future
                                } else {
                                    completedNotices.add(notice); // Completed notice if eventDate is in the past
                                }
                            } else {
                                // If the notice has no valid date, treat it as completed
                                completedNotices.add(notice);
                            }
                        }

                        // Sort both lists by eventDate in descending order (newest first)
                        upcomingNotices.sort((n1, n2) -> n2.getNotice_date().compareTo(n1.getNotice_date()));
                        completedNotices.sort((n1, n2) -> n2.getNotice_date().compareTo(n1.getNotice_date()));

                        // Log the sizes of the lists for debugging
                        Log.d("Firestore", "Upcoming Notices: " + upcomingNotices.size());
                        Log.d("Firestore", "Completed Notices: " + completedNotices.size());

                        // Update the adapters with the new list of notices
                        upcomingAdapter.updateNotices(upcomingNotices);
                        completedAdapter.updateNotices(completedNotices);
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                        Toast.makeText(NoticeDisplayActivity.this, "Failed to load notices", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Helper method to compare two Date objects
    private int compareDates(Date date1, Date date2) {
        if (date1 == null && date2 == null) return 0;
        if (date1 == null) return 1; // Treat null as later
        if (date2 == null) return -1; // Treat null as later
        return date1.compareTo(date2);
    }
}
