package com.restaurant.service;

import com.restaurant.dao.OrderDAO;
import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.model.Order;

import java.util.Arrays;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderMapper mapper = OrderMapper.INSTANCE;

    public List<OrderDTO> getAllOrders() {
        return orderDAO.readOrders(false).stream().map(mapper::toOrderDTO).toList();
    }

    public List<OrderDTO> getUnpaidOrders(int clientId) {
        return orderDAO.readUnpaidOrders(clientId).stream().map(mapper::toOrderDTO).toList();
    }

    public void createOrders(OrderReceiveDTO[] orders) {
        orderDAO.createOrder(Arrays.stream(orders).map(mapper::fromOrderReceive).toList().forEach(order -> order.setStatus(Order.StatusOrder.ORDERED)));
    }
}
