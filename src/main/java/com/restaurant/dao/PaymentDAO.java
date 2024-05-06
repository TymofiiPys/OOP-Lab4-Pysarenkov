package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Payment;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Log4j2
public class PaymentDAO {
    private Connection connection;

    public PaymentDAO() {
        this.connection = RestaurantDBConnection.getConnection();
    }

    public void createPayment(Payment payment) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO payment (client_id, time, cost) VALUES (?, ?, ?)");
            statement.setInt(1, payment.getClientId());
            statement.setTimestamp(2, payment.getTime());
            statement.setBigDecimal(3, BigDecimal.valueOf(payment.getCost()));
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when CREATING PAYMENT (" + payment.toString() + " stacktrace: ", e);
        }
    }
}
