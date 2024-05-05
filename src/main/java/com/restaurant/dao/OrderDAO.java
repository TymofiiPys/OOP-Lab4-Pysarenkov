package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.dto.OrderDisplayDTO;
import com.restaurant.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Connection connection;

    // Constructor to initialize the database connection
    public OrderDAO() {
        this.connection = RestaurantDBConnection.getConnection();
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
            // TODO (everywhere): change e.printStackTrace() to log4j's log.error("Message", e)
            e.printStackTrace();
        }
    }

    public void createOrder(List<Order> orders) {
        try {
            connection.setAutoCommit(false);
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
        } finally {
            connection.setAutoCommit(true);
        }
    }

//    public List<Order> getUnpaidOrders() {
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
//        return readOrders(true);
//    }

    public List<Order> readOrders(Order.StatusOrder status, Integer clientId) {
        List<Order> orders = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM orders ORDER BY orders.id");
            if (status != null) {
                sql
                        .append("WHERE status = ")
                        .append(status.name().toLowerCase());
            }
            if (clientId != null) {
                sql
                        .append("WHERE orders.client_id = ")
                        .append(clientId);
            }
            PreparedStatement statement = connection.prepareStatement(sql.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = Order.builder()
                        .id(resultSet.getInt("id"))
                        .clientId(resultSet.getInt("client_id"))
                        .menuId(resultSet.getInt("menu_id"))
                        .amount(resultSet.getInt("amount"))
                        .status(Order.StatusOrder.valueOf(resultSet.getString("status").toUpperCase()))
                        .build();
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

//    public List<Order> readUnpaidOrders(int id) {
//        List<Order> orders = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM orders WHERE orders.client_id = ? AND orders.status = ?";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, id);
//            statement.setObject(2, "issued_for_payment", java.sql.Types.OTHER);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Order order = Order.builder().build();
//                order.setId(resultSet.getInt("id"));
//                order.setClientId(resultSet.getInt("client_id"));
//                order.setMenuId(resultSet.getInt("menu_id"));
//                order.setAmount(resultSet.getInt("amount"));
//                order.setStatus(Order.StatusOrder.valueOf(resultSet.getString("status").toUpperCase()));
//                ;
//                orders.add(order);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orders;
//    }

    public int getClientID(OrderDisplayDTO order) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT orders.client_id FROM orders WHERE orders.id = ?");
            statement.setInt(1, order.getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("client_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateOrderStatus(List<Integer> ids, Order.StatusOrder status) {
        if (status == null) {
            return;
        }
//        PreparedStatement statement = connection.prepareStatement("UPDATE orders SET status = ? WHERE id = ?");
//        statement.setObject(1, status.name().toLowerCase(), Types.OTHER);
//        statement.setInt(2, id);
//        statement.executeUpdate();
        try {
            connection.setAutoCommit(false);
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
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
