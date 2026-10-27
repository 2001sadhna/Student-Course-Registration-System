package com.example.registration;

public class Student {
    private int studentId;
    private String name;
    private String email;
    private String phone;

    public Student(int studentId, String name,
                   String email, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Student(String name, String email, String phone) {
        this(0, name, email, phone);
    }

    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return "ID: " + studentId
                + " | Name: " + name
                + " | Email: " + email
                + " | Phone: " + phone;
    }
}