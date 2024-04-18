package com.example.dao;

import com.example.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Connection connection;

    // Constructor to initialize the database connection
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Order> getUnpaidOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Order\" WHERE status = ?");
            statement.setString(1, "ordered");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("ID"));
                order.setClientId(resultSet.getInt("Client_ID"));
                order.setMenuId(resultSet.getInt("Menu_ID"));
                order.setAmount(resultSet.getInt("amount"));
                order.setStatus(Order.StatusOrder.valueOf(resultSet.getString("status")));;
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
