package com.example.registration;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final StudentService service = new StudentService();

    public static void main(String[] args) {
        try {
            boolean running = true;

            while (running) {
                printMenu();

                int choice = readInt("Enter choice: ");

                try {
                    switch (choice) {
                        case 1 -> registerStudent();
                        case 2 -> addCourse();
                        case 3 -> enrollStudent();
                        case 4 -> viewStudents();
                        case 5 -> viewCourses();
                        case 6 -> updateStudent();
                        case 7 -> deleteStudent();
                        case 8 -> updateCourse();
                        case 9 -> deleteCourse();
                        case 10 -> showStudentsInCourse();
                        case 11 -> showStudentCourses();
                        case 12 -> showReport();
                        case 0 -> running = false;
                        default -> System.out.println(
                                "Invalid menu choice.");
                    }
                } catch (SQLException e) {
                    System.out.println(
                            "Database error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "Invalid input: " + e.getMessage());
                }
            }
        } finally {
            sc.close();
            DBConnection.closePool();
            System.out.println("Application closed.");
        }
    }

    private static void printMenu() {
        System.out.println("\n===== STUDENT COURSE REGISTRATION =====");
        System.out.println("1. Register Student");
        System.out.println("2. Add Course");
        System.out.println("3. Enroll Student in Course");
        System.out.println("4. View All Students");
        System.out.println("5. View All Courses");
        System.out.println("6. Update Student");
        System.out.println("7. Delete Student");
        System.out.println("8. Update Course");
        System.out.println("9. Delete Course");
        System.out.println("10. Students in a Particular Course");
        System.out.println("11. Courses Taken by a Student");
        System.out.println("12. Student-Course JOIN Report");
        System.out.println("0. Exit");
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid integer.");
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static BigDecimal readFee() {
        try {
            return new BigDecimal(readText("Course fee: "));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Enter a valid course fee.");
        }
    }

    private static void registerStudent() throws SQLException {
        String name = readText("Student name: ");
        String email = readText("Email: ");
        String phone = readText("Phone: ");

        service.registerStudent(name, email, phone);
        System.out.println("Student registered successfully!");
    }

    private static void addCourse() throws SQLException {
        String name = readText("Course name: ");
        int duration = readInt("Duration in months: ");
        BigDecimal fee = readFee();

        service.addCourse(name, duration, fee);
        System.out.println("Course added successfully!");
    }

    private static void enrollStudent() throws SQLException {
        int studentId = readInt("Student ID: ");
        int courseId = readInt("Course ID: ");

        service.enrollments().enrollStudent(studentId, courseId);
    }

    private static void viewStudents() throws SQLException {
        var students = service.students().getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void viewCourses() throws SQLException {
        var courses = service.courses().getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        }
        for (Course c : courses) {
            System.out.println(c);
        }
    }

    private static void updateStudent() throws SQLException {
        int id = readInt("Student ID to update: ");
        Student existing = service.students().getStudent(id);

        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }

        String name = readText("New name: ");
        String email = readText("New email: ");
        String phone = readText("New phone: ");

        service.students().updateStudent(
                new Student(id, name, email, phone));
        System.out.println("Student updated successfully!");
    }

    private static void deleteStudent() throws SQLException {
        int id = readInt("Student ID to delete: ");
        service.students().deleteStudent(id);
        System.out.println("Student deleted successfully!");
    }

    private static void updateCourse() throws SQLException {
        int id = readInt("Course ID to update: ");
        String name = readText("New course name: ");
        int duration = readInt("New duration in months: ");
        BigDecimal fee = readFee();

        if (name.isBlank() || duration <= 0
                || fee.signum() < 0) {
            throw new IllegalArgumentException(
                    "Invalid course details.");
        }

        service.courses().updateCourse(
                new Course(id, name, duration, fee));
        System.out.println("Course updated successfully!");
    }

    private static void deleteCourse() throws SQLException {
        int id = readInt("Course ID to delete: ");
        service.courses().deleteCourse(id);
        System.out.println("Course deleted successfully!");
    }

    private static void showStudentsInCourse()
            throws SQLException {
        int id = readInt("Course ID: ");
        service.enrollments().showStudentsInCourse(id);
    }

    private static void showStudentCourses()
            throws SQLException {
        int id = readInt("Student ID: ");
        service.enrollments().showStudentCourses(id);
    }

    private static void showReport() throws SQLException {
        service.enrollments().showStudentCourseReport();
    }
}