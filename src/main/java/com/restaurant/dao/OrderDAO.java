package com.restaurant.dao;

import com.restaurant.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Connection connection;

    // Constructor to initialize the database connection
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    public void createOrder(Order order) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO orders (client_id, menu_id, amount, status) VALUES (?, ?, ?, ?)");
            statement.setInt(1, order.getClientId());
            statement.setInt(2, order.getMenuId());
            statement.setInt(3, order.getAmount());
            statement.setString(4, order.getStatus().name().toLowerCase());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createOrder(List<Order> orders) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO orders (client_id, menu_id, amount, status) VALUES (?, ?, ?, ?)");
            for (Order order : orders) {
                if (order.getAmount() < 1) {
                    continue;
                }
                statement.setInt(1, order.getClientId());
                statement.setInt(2, order.getMenuId());
                statement.setInt(3, order.getAmount());
                statement.setObject(4, order.getStatus().name().toLowerCase(), java.sql.Types.OTHER);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        }
    }

    public List<Order> getUnpaidOrders() {
//        List<Order> orders = new ArrayList<>();
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders WHERE status = ?");
//            statement.setString(1, "ordered");
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Order order = new Order();
//                order.setId(resultSet.getInt("id"));
//                order.setClientId(resultSet.getInt("client_id"));
//                order.setMenuId(resultSet.getInt("menu_id"));
//                order.setAmount(resultSet.getInt("amount"));
//                order.setStatus(Order.StatusOrder.valueOf(resultSet.getString("status").toUpperCase()));;
//                orders.add(order);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return readOrders(true);
    }

    public List<Order> readOrders(boolean readUnpaid) {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM orders ORDER BY orders.id";
            if (readUnpaid) {
                sql += "WHERE status = ordered";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, "ordered");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setClientId(resultSet.getInt("client_id"));
                order.setMenuId(resultSet.getInt("menu_id"));
                order.setAmount(resultSet.getInt("amount"));
                order.setStatus(Order.StatusOrder.valueOf(resultSet.getString("status").toUpperCase()));
                ;
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> readUnpaidOrders(int id) {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM orders WHERE orders.client_id = ? AND orders.status = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setObject(2, "issued_for_payment", java.sql.Types.OTHER);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setClientId(resultSet.getInt("client_id"));
                order.setMenuId(resultSet.getInt("menu_id"));
                order.setAmount(resultSet.getInt("amount"));
                order.setStatus(Order.StatusOrder.valueOf(resultSet.getString("status").toUpperCase()));
                ;
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateOrderStatus(List<Integer> ids, Order.StatusOrder status) throws SQLException {
        if (status == null) {
            return;
        }
//        PreparedStatement statement = connection.prepareStatement("UPDATE orders SET status = ? WHERE id = ?");
//        statement.setObject(1, status.name().toLowerCase(), Types.OTHER);
//        statement.setInt(2, id);
//        statement.executeUpdate();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE orders SET status = ? WHERE id = ?");
            for (int id : ids) {
                statement.setObject(1, status.name().toLowerCase(), Types.OTHER);
                statement.setInt(2, id);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        }
    }
}
