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

    public void createMenu(Menu menu) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO menu (name, meal_drink, cost) VALUES (?, ?, ?)");
            statement.setString(1, menu.getName());
            statement.setBoolean(2, menu.isMealOrDrink());
            statement.setDouble(3, menu.getCost());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Menu> readMenu() {
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

    public void updateMenu(Menu menu) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE menu SET name = ?, meal = ?, cost = ? WHERE id = ?");
            statement.setString(1, menu.getName());
            statement.setBoolean(2, menu.isMealOrDrink());
            statement.setDouble(3, menu.getCost());
            statement.setInt(4, menu.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(int menuId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM menu WHERE id = ?");
            statement.setInt(1, menuId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
