package com.example.dao;

import com.example.model.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    private Connection connection;

    // Constructor to initialize the database connection
    public MenuDAO(Connection connection) {
        this.connection = connection;
    }
    public List<Menu> getAllMenus() {
        List<Menu> menus = new ArrayList<>();
        try {
            String sql = "SELECT * FROM menu";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Menu menu = new Menu();
                menu.setId(resultSet.getInt("id"));
                menu.setName(resultSet.getString("name"));
                menu.setMealOrDrink(resultSet.getBoolean("meal_drink"));
                menu.setCost(resultSet.getDouble("cost"));
                menus.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menus;
    }
}

