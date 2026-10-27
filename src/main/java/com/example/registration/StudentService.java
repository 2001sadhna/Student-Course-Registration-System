package com.example.registration;

import java.math.BigDecimal;
import java.sql.SQLException;

public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public void registerStudent(String name, String email,
                                String phone) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Student name is required.");
        }
        if (email == null
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Enter a valid email address.");
        }

        studentDAO.addStudent(
                new Student(name.trim(), email.trim(), phone));
    }

    public void addCourse(String name, int duration,
                          BigDecimal fee) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Course name is required.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than zero.");
        }
        if (fee == null || fee.signum() < 0) {
            throw new IllegalArgumentException(
                    "Fee cannot be negative.");
        }

        courseDAO.addCourse(
                new Course(name.trim(), duration, fee));
    }

    public StudentDAO students() {
        return studentDAO;
    }

    public CourseDAO courses() {
        return courseDAO;
    }

    public EnrollmentDAO enrollments() {
        return enrollmentDAO;
    }
}
