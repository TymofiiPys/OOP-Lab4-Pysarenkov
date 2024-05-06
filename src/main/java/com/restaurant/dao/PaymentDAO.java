package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Payment;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.*;

@Log4j2
public class PaymentDAO {
    private final Connection connection;

    public PaymentDAO() {
        this.connection = RestaurantDBConnection.getConnection();
    }

    public Payment createPayment(Payment payment) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO payment (client_id, time, cost) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, payment.getClientId());
            statement.setTimestamp(2, payment.getTime());
            statement.setBigDecimal(3, BigDecimal.valueOf(payment.getCost()));
            statement.executeUpdate();
            ResultSet idRS = statement.getGeneratedKeys();
            idRS.next();
            payment.setId(idRS.getInt(1));
        } catch (SQLException e) {
            log.error("SQLException when CREATING PAYMENT (" + payment.toString() + " stacktrace: ", e);
            return null;
        }
        return payment;
    }
}
