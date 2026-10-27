package com.example.registration;

import java.math.BigDecimal;

public class Course {
    private int courseId;
    private String courseName;
    private int durationMonths;
    private BigDecimal fee;

    public Course(int courseId, String courseName,
                  int durationMonths, BigDecimal fee) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.durationMonths = durationMonths;
        this.fee = fee;
    }

    public Course(String courseName, int durationMonths,
                  BigDecimal fee) {
        this(0, courseName, durationMonths, fee);
    }

    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getDurationMonths() { return durationMonths; }
    public BigDecimal getFee() { return fee; }

    @Override
    public String toString() {
        return "ID: " + courseId
                + " | Course: " + courseName
                + " | Duration: " + durationMonths + " months"
                + " | Fee: " + fee;
    }
}