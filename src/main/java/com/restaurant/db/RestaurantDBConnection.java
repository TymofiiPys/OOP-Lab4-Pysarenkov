package com.restaurant.db;

import lombok.SneakyThrows;

import java.sql.*;

public class RestaurantDBConnection {
    private static Connection conn;
//    private RestaurantDBConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("org.postgresql.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
//                "postgres", "password");
//    }

    @SneakyThrows({ClassNotFoundException.class, SQLException.class})
    public static Connection getConnection() {
        if(conn == null) {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "password");
            conn.setAutoCommit(false);
        }
        return conn;
    }
}
