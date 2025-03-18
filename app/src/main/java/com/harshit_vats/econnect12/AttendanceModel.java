package com.harshit_vats.econnect12;


public class AttendanceModel {
    private String studentName;
    private String rollNumber;
    private String className;
    private String date;
    private boolean isPresent;

    public AttendanceModel() {}

    public AttendanceModel(String className, String date, String studentName, String rollNumber, boolean isPresent) {
        this.className = className;
        this.date = date;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.isPresent = isPresent;
    }

    public String getStudentName() { return studentName; }
    public String getRollNumber() { return rollNumber; }
    public String getClassName() { return className; }
    public String getDate() { return date; }
    public boolean isPresent() { return isPresent; }
}
