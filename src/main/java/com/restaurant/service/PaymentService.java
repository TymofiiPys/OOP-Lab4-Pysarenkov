package com.restaurant.service;

import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.PaymentDAO;
import com.restaurant.dto.PaymentDTO;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.mapper.PaymentMapper;
import com.restaurant.model.Payment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

    public PaymentDTO createPayment(List<PaymentDTO> payment) {
        double cost = payment.stream().map(PaymentDTO::getCost).reduce(0., Double::sum);
        Payment createdPayment = paymentDAO.createPayment(Payment.builder()
                .clientId(payment.getFirst().getClientId())
                .time(new Timestamp(new Date().getTime()))
                .cost(cost)
                .build());
        if(createdPayment == null) return null;
        return paymentMapper.toPaymentDTO(createdPayment);
    }
}
