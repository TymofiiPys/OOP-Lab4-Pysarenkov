package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Menu;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class MenuDAO {

    private final Connection connection;

    // Constructor to initialize the database connection
    public MenuDAO() {
        this.connection = RestaurantDBConnection.getConnection();
    }

    public Menu createMenu(Menu menu) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO menu (name, meal_drink, cost) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, menu.getName());
            statement.setBoolean(2, menu.isMealOrDrink());
            statement.setDouble(3, menu.getCost());
            statement.executeUpdate();
            ResultSet idRS = statement.getGeneratedKeys();
            idRS.next();
            menu.setId(idRS.getInt(1));
        } catch (SQLException e) {
            log.error("SQLException when CREATING MENU (" + menu.toString() + " stacktrace: ", e);
            return null;
        }
        return menu;
    }

    public List<Menu> readMenu(Integer id) {
        List<Menu> menus = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM menu");
            if (id != null) {
                sql
                        .append(" WHERE menu.id = ")
                        .append(id);
            }
            PreparedStatement statement = connection.prepareStatement(sql.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Menu menu = Menu.builder().build();
                menu.setId(resultSet.getInt("id"));
                menu.setName(resultSet.getString("name"));
                menu.setMealOrDrink(resultSet.getBoolean("meal_drink"));
                menu.setCost(resultSet.getDouble("cost"));
                menus.add(menu);
            }
        } catch (SQLException e) {
            log.error("SQLException when READING MENU, stacktrace: ", e);
            return null;
        }
        return menus;
    }

//    public String getMenuItemName(int id) {
//        String name = "";
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT menu.name FROM menu WHERE menu.id = ?");
//            statement.setInt(1, id);
//            ResultSet resultSet = statement.executeQuery();
//            resultSet.next();
//            name = resultSet.getString("name");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return name;
//    }

    public Optional<Menu> getMenuItem(int menuId) {
        try {
            String sql = "SELECT * FROM menu WHERE menu.id = " + menuId;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return Optional.of(
                        Menu.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .mealOrDrink(resultSet.getBoolean("meal_drink"))
                                .cost(resultSet.getDouble("cost"))
                                .build()
                );
            else
                return Optional.empty();
        } catch (SQLException e) {
            log.error("SQLException when READING MENU with ID ("
                    + menuId
                    + ") stacktrace: ", e);
            return Optional.empty();
        }
    }

    public int updateMenu(Menu menu) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE menu SET name = ?, meal = ?, cost = ? WHERE id = ?");
            statement.setString(1, menu.getName());
            statement.setBoolean(2, menu.isMealOrDrink());
            statement.setDouble(3, menu.getCost());
            statement.setInt(4, menu.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when READING MENU with ID ("
                    + menu.toString()
                    + "), stacktrace: ", e);
            return -1;
        }
    }

    public int deleteMenu(int menuId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM menu WHERE id = ?");
            statement.setInt(1, menuId);
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when DELETING MENU with ID"
                    + menuId
                    + "), stacktrace: ", e);
            return -1;
        }
    }
}

