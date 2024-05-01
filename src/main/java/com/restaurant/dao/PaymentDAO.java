package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Payment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAO {
    private Connection connection;

    public PaymentDAO() {
        this.connection = RestaurantDBConnection.getConnection();
    }

    public void createPayment(Payment payment) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("INSERT INTO payment (client_id, time, cost) VALUES (?, ?, ?)");
        statement.setInt(1, payment.getClientId());
        statement.setTimestamp(2, payment.getTime());
        statement.setBigDecimal(3, BigDecimal.valueOf(payment.getCost()));
        statement.executeUpdate();
    }
}
