package com.restaurant.service;

import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.PaymentDAO;
import com.restaurant.dto.OrderDisplayDTO;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.model.Payment;

import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderMapper mapper = OrderMapper.INSTANCE;

    public void createPayment(List<OrderDisplayDTO> orders) {
        double cost = orders.stream().map(OrderDisplayDTO::getCost).reduce(0., Double::sum);
        paymentDAO.createPayment(Payment.builder()
                .clientId(orders.getFirst().getCId()))
    }
}
