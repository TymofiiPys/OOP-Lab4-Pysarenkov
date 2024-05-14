package com.restaurant.service;

import com.restaurant.dao.ClientDAO;
import com.restaurant.dao.PaymentDAO;
import com.restaurant.dto.PaymentCreateDTO;
import com.restaurant.dto.PaymentDisplayDTO;
import com.restaurant.mapper.PaymentMapper;
import com.restaurant.model.Payment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final PaymentMapper mapper = PaymentMapper.INSTANCE;

    public PaymentDisplayDTO createPayment(List<PaymentCreateDTO> payment) {
        double cost = payment.stream().map(PaymentCreateDTO::getCost).reduce(0., Double::sum);
        Payment createdPayment = paymentDAO.createPayment(Payment.builder()
                .clientId(payment.getFirst().getClientId())
                .time(new Timestamp(new Date().getTime()))
                .cost(cost)
                .build());
        if(createdPayment == null) return null;
        return mapper.toPaymentDisplayDTO(createdPayment);
    }

    public List<PaymentDisplayDTO> getAllPayments() {
        List<Payment> payments = paymentDAO.readPayment();
        List<PaymentDisplayDTO> paymentsToDisplay = payments.stream().map(mapper::toPaymentDisplayDTO).toList();
        for (var payment : paymentsToDisplay) {
            payment.setClientName(clientDAO.getClientEmail(payment.getClientId()).orElse(""));
        }
        return paymentsToDisplay;
    }
}
