package com.harshit_vats.econnect12;

public class HomeworkModel {
    private String date;
    private String faculty_name;
    private String faculty_id;
    private String subject;
    private String description;

    // **Empty constructor required for Firestore deserialization**
    public HomeworkModel() {
    }

    public HomeworkModel(String date, String faculty_name, String faculty_id, String subject, String description) {
        this.date = date;
        this.faculty_name = faculty_name;
        this.faculty_id = faculty_id;
        this.subject = subject;
        this.description = description;
    }

    // **Getters and Setters**
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    public String getFaculty_id() {
        return faculty_id;
    }

    public void setFaculty_id(String faculty_id) {
        this.faculty_id = faculty_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
