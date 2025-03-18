package com.harshit_vats.econnect12;

public class StudentModel {
    private String id; // Unique Firestore ID
    private String name;
    private String admissionNumber;
    private String rollNumber;
    private boolean present; // Tracks attendance

    // Default constructor required for Firebase
    public StudentModel() {}

    // Constructor for full data
    public StudentModel(String id, String name, String admissionNumber, String rollNumber) {
        this.id = id != null ? id : "";
        this.name = name != null ? name : "";
        this.admissionNumber = admissionNumber != null ? admissionNumber : "N/A";
        this.rollNumber = rollNumber != null ? rollNumber : "N/A";
        this.present = false; // Default: Absent
    }

    // Constructor when only ID and name are available
    public StudentModel(String id, String name) {
        this(id, name, "N/A", "N/A"); // Call main constructor with default values
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAdmissionNumber() { return admissionNumber; }
    public String getRollNumber() { return rollNumber; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }
}
