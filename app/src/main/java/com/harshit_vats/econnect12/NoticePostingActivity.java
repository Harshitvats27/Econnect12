package com.harshit_vats.econnect12;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class NoticePostingActivity extends AppCompatActivity {

    private EditText noticeTitle, noticeDescription;
    private TextView noticeDate;
    private Button postNoticeButton;
    private FirebaseFirestore db;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_posting);

        noticeTitle = findViewById(R.id.noticeTitle);
        noticeDescription = findViewById(R.id.noticeDescription);
        noticeDate = findViewById(R.id.noticeDate);
        postNoticeButton = findViewById(R.id.postNoticeButton);

        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        // Set current date on TextView
        updateDateDisplay();

        // Open DatePickerDialog when user clicks the date
        noticeDate.setOnClickListener(v -> showDatePickerDialog());

        postNoticeButton.setOnClickListener(v -> postNotice());
    }

    private void showDatePickerDialog() {
        // DatePickerDialog to pick the date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Update the calendar with selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        // Format date to display in TextView
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        noticeDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void postNotice() {
        String title = noticeTitle.getText().toString();
        String description = noticeDescription.getText().toString();
        String inputDate = noticeDate.getText().toString(); // e.g., "23/03/2025"

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert calendar to Firestore's Timestamp
        java.util.Date date = calendar.getTime();

        // Check if the selected date is in the past
        if (date.before(Calendar.getInstance().getTime())) {
            Toast.makeText(this, "Cannot post a notice for a past date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data for Firestore
        Map<String, Object> notice = new HashMap<>();
        notice.put("notice_title", title);
        notice.put("notice_description", description);
        notice.put("notice_date", date); // Use the Date object directly for Firestore
        notice.put("posted_by", "Admin");

        // Upload to Firestore
        db.collection("notices")
                .add(notice)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(NoticePostingActivity.this, "Notice posted successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Optionally close the activity after posting
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NoticePostingActivity.this, "Failed to post notice", Toast.LENGTH_SHORT).show();
                });
    }
}
