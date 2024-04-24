package com.example.dao;

import com.example.model.Payment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PaymentDAO {
    private Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    public void createPayment(Payment payment) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("INSERT INTO payment (client_id, time, cost) VALUES (?, ?, ?)");
        statement.setInt(1, payment.getClientId());
//            statement.setObject(2, payment.getTime(), Types.TIMESTAMP);
        statement.setTimestamp(2, payment.getTime());
        statement.setBigDecimal(3, BigDecimal.valueOf(payment.getCost()));
        statement.executeUpdate();
    }
}
