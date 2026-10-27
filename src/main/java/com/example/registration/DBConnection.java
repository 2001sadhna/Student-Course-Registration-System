package com.example.registration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class DBConnection {

    private static final HikariDataSource DATA_SOURCE;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(
                "jdbc:mysql://localhost:3306/student_course_db"
                        + "?serverTimezone=UTC"
        );
        config.setUsername(
                System.getenv().getOrDefault("DB_USER", "root")
        );
        config.setPassword(
                System.getenv().getOrDefault("DB_PASSWORD", "root1234")
        );

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);

        DATA_SOURCE = new HikariDataSource(config);
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public static void closePool() {
        DATA_SOURCE.close();
    }
}
