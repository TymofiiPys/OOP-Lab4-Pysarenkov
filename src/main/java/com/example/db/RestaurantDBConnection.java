package com.example.db;

import java.sql.*;

public class RestaurantDBConnection {
    private static Connection conn;
//    private RestaurantDBConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("org.postgresql.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
//                "postgres", "password");
//    }
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if(conn == null) {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "password");
        }
        return conn;
    }
}
