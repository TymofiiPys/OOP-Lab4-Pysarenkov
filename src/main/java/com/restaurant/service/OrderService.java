package com.restaurant.service;

import com.restaurant.dao.ClientDAO;
import com.restaurant.dao.MenuDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.dto.OrderDTO;
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
        return toOrderDisplayDTOList(orderDAO.readOrders(null, null));
    }

    public List<OrderDisplayDTO> getOrdersFilteredByStatus(String status) {
        return toOrderDisplayDTOList(orderDAO.readOrders(Order.StatusOrder.valueOf(status), null));
    }

    public List<OrderDisplayDTO> getOrdersFilteredByStatusAndId(String status, int clientId) {
        return toOrderDisplayDTOList(orderDAO.readOrders(Order.StatusOrder.valueOf(status), clientId));

    }

    private List<OrderDisplayDTO> toOrderDisplayDTOList(List<Order> orders) {
        if(orders == null) return null;
        List<OrderDisplayDTO> orderDisplay = new ArrayList<>();
        for (Order order : orders) {
            Optional<Menu> orderedItem = menuDAO.getMenuItem(order.getMenuId());
            Optional<String> clientName = clientDAO.getClientName(order.getClientId());
            if(orderedItem.isEmpty() || clientName.isEmpty()) {
                continue;
            }
            orderDisplay.add(
                    OrderDisplayDTO.builder()
                            .id(order.getId())
                            .clientName(clientName.get())
                            .menuItemName(orderedItem.get().getName())
                            .amount(order.getAmount())
                            .cost(order.getAmount() * orderedItem.get().getCost())
                            .build()
            );
        }
        return orderDisplay;
    }

    public List<OrderDTO> createOrders(List<OrderReceiveDTO> orders) {
        List<Order> ordersMapped = orders.stream().map(mapper::fromOrderReceive).toList();
        ordersMapped.forEach(order -> order.setStatus(Order.StatusOrder.ORDERED));
        List<Order> createdOrders = orderDAO.createOrder(ordersMapped);
        if(createdOrders == null) return null;
        return createdOrders.stream().map(mapper::toOrderDTO).toList();
    }

    public int updateOrderStatus(List<Integer> orderIds, Order.StatusOrder status) {
        return orderDAO.updateOrderStatus(orderIds, status);
    }
}
