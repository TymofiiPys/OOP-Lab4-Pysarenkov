package com.example.dao;

import com.example.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PaymentDAO {
    private Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    public void createPayment(Payment payment) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO payment (client_id, time, cost) VALUES (?, ?, ?)");
            statement.setInt(1, payment.getClientId());
            statement.setObject(2, payment.getTime(), Types.TIMESTAMP);
            statement.setDouble(3, payment.getCost());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
