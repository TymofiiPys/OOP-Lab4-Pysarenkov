package com.restaurant.service;

import com.restaurant.dao.ClientDAO;
import com.restaurant.dao.MenuDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.dto.OrderDisplayDTO;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.model.Menu;
import com.restaurant.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final MenuDAO menuDAO = new MenuDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final OrderMapper mapper = OrderMapper.INSTANCE;

    public List<OrderDisplayDTO> getAllOrders() {
        return toOrderDispleyDTOList(orderDAO.readOrders(null, null));
    }

    public List<OrderDisplayDTO> getOrdersFilteredByStatus(String status) {
        return toOrderDispleyDTOList(orderDAO.readOrders(Order.StatusOrder.valueOf(status), null));
    }

    public List<OrderDisplayDTO> getOrdersFilteredByStatusAndId(String status, int clientId) {
        return toOrderDispleyDTOList(orderDAO.readOrders(Order.StatusOrder.valueOf(status), clientId));

    }

    private List<OrderDisplayDTO> toOrderDispleyDTOList(List<Order> orders) {
        List<OrderDisplayDTO> orderDisplay = new ArrayList<>();
        for (Order order : orders) {
            Menu orderedItem = menuDAO.getMenuItem(order.getMenuId());
            Optional<String> clientName = clientDAO.getClientName(order.getClientId());
            if (clientName.isEmpty()) {
                //return Optional.empty();
            }
            orderDisplay.add(
                    OrderDisplayDTO.builder()
                            .id(order.getId())
                            .clientName(clientName.get())
                            .menuItemName(orderedItem.getName())
                            .amount(order.getAmount())
                            .cost(order.getAmount() * orderedItem.getCost())
                            .build()
            );
        }
        return orderDisplay;
    }

    public void createOrders(List<OrderReceiveDTO> orders) {
        List<Order> ordersMapped = orders.stream().map(mapper::fromOrderReceive).toList();
        ordersMapped.forEach(order -> order.setStatus(Order.StatusOrder.ORDERED));
        orderDAO.createOrder(ordersMapped);
    }

    public void updateOrderStatus(List<Integer> orderIds, Order.StatusOrder status) {
        orderDAO.updateOrderStatus(orderIds, status);
    }
}
