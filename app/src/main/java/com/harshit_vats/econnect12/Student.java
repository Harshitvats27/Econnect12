package com.harshit_vats.econnect12;

public class Student {
    private String registrationNumber;
    private String name;
    private String email;
    private boolean isPresent;

    public Student() {
        // Empty constructor for Firebase
    }

    public Student(String registrationNumber, String name, String email, boolean isPresent) {
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.email = email;
        this.isPresent = isPresent;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
