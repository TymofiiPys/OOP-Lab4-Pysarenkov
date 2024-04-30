package com.restaurant.db;

import lombok.SneakyThrows;

import java.sql.*;

public class RestaurantDBConnection {
    private static Connection conn;

    @SneakyThrows({ClassNotFoundException.class, SQLException.class})
    public static Connection getConnection() {
        if(conn == null) {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "password");
            conn.setAutoCommit(false);
        }
        return conn;
    }
}
