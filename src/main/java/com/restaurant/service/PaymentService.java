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

    public PaymentDisplayDTO createPayment(PaymentCreateDTO payment, int clientId) {
        Payment createdPayment = paymentDAO.createPayment(Payment.builder()
                .clientId(clientId)
                .time(new Timestamp(new Date().getTime()))
                .cost(payment.getCost())
                .build());
        if(createdPayment == null) return null;
        PaymentDisplayDTO ret = mapper.toPaymentDisplayDTO(createdPayment);
        ret.setTimeStr(ret.getTime().toString());
        return ret;
    }

    public List<PaymentDisplayDTO> getAllPayments() {
        List<Payment> payments = paymentDAO.readPayment();
        List<PaymentDisplayDTO> paymentsToDisplay = payments.stream().map(mapper::toPaymentDisplayDTO).toList();
        for (var payment : paymentsToDisplay) {
            payment.setClientName(clientDAO.getClientEmail(payment.getClientId()).orElse(""));
            payment.setTimeStr(payment.getTime().toString());
        }
        return paymentsToDisplay;
    }
}
