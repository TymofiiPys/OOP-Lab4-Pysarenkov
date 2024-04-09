package com.example.db;

import java.sql.*;

public class RestaurantDB {
    public RestaurantDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "password");
        conn.setAutoCommit(false);
        if(conn != null) {

        }
        conn.close();
    }

    public static void main(String[] args) {
        try {
            new RestaurantDB();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
