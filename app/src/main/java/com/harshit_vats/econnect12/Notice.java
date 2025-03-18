package com.harshit_vats.econnect12;

import android.util.Log;
import java.util.Date;

public class Notice {
    private String notice_title;
    private String notice_description;
    private Date notice_date; // Changed to Date
    private String posted_by;

    // Constructor
    public Notice(String notice_title, String notice_description, Date notice_date, String posted_by) {
        this.notice_title = notice_title;
        this.notice_description = notice_description;
        this.notice_date = notice_date;
        this.posted_by = posted_by;

        // Log constructor data
        Log.d("Notice", "Notice created: " + this.toString());
    }

    // Getters and Setters
    public String getNotice_title() {
        Log.d("Notice", "Getting notice title: " + notice_title);
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        Log.d("Notice", "Setting notice title: " + notice_title);
        this.notice_title = notice_title;
    }

    public String getNotice_description() {
        Log.d("Notice", "Getting notice description: " + notice_description);
        return notice_description;
    }

    public void setNotice_description(String notice_description) {
        Log.d("Notice", "Setting notice description: " + notice_description);
        this.notice_description = notice_description;
    }

    public Date getNotice_date() {
        Log.d("Notice", "Getting notice date: " + notice_date);
        return notice_date;
    }

    public void setNotice_date(Date notice_date) {
        Log.d("Notice", "Setting notice date: " + notice_date);
        this.notice_date = notice_date;
    }

    public String getPosted_by() {
        Log.d("Notice", "Getting posted by: " + posted_by);
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        Log.d("Notice", "Setting posted by: " + posted_by);
        this.posted_by = posted_by;
    }

    // toString() method
    @Override
    public String toString() {
        return "Notice{" +
                "notice_title='" + notice_title + '\'' +
                ", notice_description='" + notice_description + '\'' +
                ", notice_date=" + notice_date +
                ", posted_by='" + posted_by + '\'' +
                '}';
    }

    // equals() method to compare Notice objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Notice notice = (Notice) obj;
        return notice_title.equals(notice.notice_title) &&
                notice_description.equals(notice.notice_description) &&
                notice_date.equals(notice.notice_date) &&
                posted_by.equals(notice.posted_by);
    }
}
