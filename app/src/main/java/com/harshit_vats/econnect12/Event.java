package com.harshit_vats.econnect12;

public class Event {

    private String event_name;
    private String description;
    private String event_date;
    private String location;
    private String event_time;
    private String category;
    private String posted_by;

    // Default constructor (required for Firestore)
    public Event() {
    }

    // Constructor to initialize event fields
    public Event(String event_name, String description, String event_date, String location, String event_time, String category, String posted_by) {
        this.event_name = event_name;
        this.description = description;
        this.event_date = event_date;
        this.location = location;
        this.event_time = event_time;
        this.category = category;
        this.posted_by = posted_by;
    }

    // Getters and setters
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }
}
