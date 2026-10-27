package com.example.registration;

import java.sql.*;

public class EnrollmentDAO {

    // Enroll a student using a transaction.
    public void enrollStudent(int studentId, int courseId)
            throws SQLException {

        String checkStudent =
                "SELECT 1 FROM students WHERE student_id=?";
        String checkCourse =
                "SELECT 1 FROM courses WHERE course_id=?";
        String insert =
                "INSERT INTO enrollments"
                        + "(student_id,course_id,enrollment_date) "
                        + "VALUES(?,?,CURRENT_DATE)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps =
                             con.prepareStatement(checkStudent)) {
                    ps.setInt(1, studentId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Student ID does not exist.");
                        }
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(checkCourse)) {
                    ps.setInt(1, courseId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Course ID does not exist.");
                        }
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(insert)) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, courseId);
                    ps.executeUpdate();
                }

                con.commit();
                System.out.println("Enrollment successful!");

            } catch (SQLException | RuntimeException ex) {
                try {
                    con.rollback();
                    System.out.println(
                            "Transaction rolled back.");
                } catch (SQLException rollbackError) {
                    ex.addSuppressed(rollbackError);
                }
                throw ex;

            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // Closing the connection returns it to the pool.
                }
            }
        }
    }

    // Students enrolled in a particular course.
    public void showStudentsInCourse(int courseId)
            throws SQLException {

        String sql =
                "SELECT s.student_id, s.name, s.email, c.course_name "
                        + "FROM students s "
                        + "JOIN enrollments e ON s.student_id=e.student_id "
                        + "JOIN courses c ON c.course_id=e.course_id "
                        + "WHERE c.course_id=? ORDER BY s.name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf(
                            "Student ID: %d | Name: %s | Email: %s"
                                    + " | Course: %s%n",
                            rs.getInt("student_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("course_name")
                    );
                }
                if (!found) {
                    System.out.println(
                            "No enrollments found for this course.");
                }
            }
        }
    }

    // Call the MySQL stored procedure.
    public void showStudentCourses(int studentId)
            throws SQLException {

        try (Connection con = DBConnection.getConnection();
             CallableStatement cs =
                     con.prepareCall("{CALL GetStudentCourses(?)}")) {

            cs.setInt(1, studentId);

            try (ResultSet rs = cs.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf(
                            "%s is enrolled in %s on %s%n",
                            rs.getString("name"),
                            rs.getString("course_name"),
                            rs.getDate("enrollment_date")
                    );
                }
                if (!found) {
                    System.out.println(
                            "No courses found for this student.");
                }
            }
        }
    }

    // Report showing student names with their course names.
    public void showStudentCourseReport() throws SQLException {
        String sql =
                "SELECT s.student_id, s.name, c.course_name, "
                        + "e.enrollment_date "
                        + "FROM students s "
                        + "JOIN enrollments e ON s.student_id=e.student_id "
                        + "JOIN courses c ON c.course_id=e.course_id "
                        + "ORDER BY s.name, c.course_name";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println(
                    "\nID | Student Name | Course Name | Enrollment Date");
            System.out.println(
                    "---------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s%n",
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("course_name"),
                        rs.getDate("enrollment_date"));
            }
        }
    }
}