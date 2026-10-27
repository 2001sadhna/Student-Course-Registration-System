package com.example.registration;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void addCourse(Course c) throws SQLException {
        String sql = "INSERT INTO courses"
                + "(course_name,duration_months,fee) "
                + "VALUES(?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getDurationMonths());
            ps.setBigDecimal(3, c.getFee());
            ps.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("duration_months"),
                        rs.getBigDecimal("fee")
                ));
            }
        }
        return list;
    }

    public void updateCourse(Course c) throws SQLException {
        String sql = "UPDATE courses SET course_name=?, "
                + "duration_months=?, fee=? WHERE course_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getDurationMonths());
            ps.setBigDecimal(3, c.getFee());
            ps.setInt(4, c.getCourseId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Course ID not found.");
            }
        }
    }

    public void deleteCourse(int id) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Course ID not found.");
            }
        }
    }
}
