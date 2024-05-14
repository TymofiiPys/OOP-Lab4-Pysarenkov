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

    public List<OrderDisplayDTO> getOrdersFilteredByStatus(String statusStr) {
        Optional<Order.StatusOrder> status = checkStatus(statusStr);
        if (status.isEmpty()) return null;
        return toOrderDisplayDTOList(orderDAO.readOrders(status.get(), null));
    }

    public List<OrderDisplayDTO> getOrdersFilteredByStatusAndId(String statusStr, int clientId) {
        Optional<Order.StatusOrder> status = checkStatus(statusStr);
        if (status.isEmpty()) return null;
        return toOrderDisplayDTOList(orderDAO.readOrders(status.get(), clientId));
    }

    private Optional<Order.StatusOrder> checkStatus(String statusStr) {
        try {
            Order.StatusOrder status = Order.StatusOrder.valueOf(statusStr);
            return Optional.of(status);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private List<OrderDisplayDTO> toOrderDisplayDTOList(List<Order> orders) {
        if (orders == null) return null;
        List<OrderDisplayDTO> orderDisplay = new ArrayList<>();
        for (Order order : orders) {
            Optional<Menu> orderedItem = menuDAO.getMenuItem(order.getMenuId());
            Optional<String> clientName = clientDAO.getClientEmail(order.getClientId());
            if (orderedItem.isEmpty() || clientName.isEmpty()) {
                continue;
            }
            orderDisplay.add(
                    OrderDisplayDTO.builder()
                            .id(order.getId())
                            .clientName(clientName.get())
                            .menuItemName(orderedItem.get().getName())
                            .amount(order.getAmount())
                            .status(order.getStatus())
                            .cost(order.getAmount() * orderedItem.get().getCost())
                            .build()
            );
        }
        return orderDisplay;
    }

    private List<Order> fromOrderReceiveDTO(OrderReceiveDTO orders, int clientId) {
        List<Order> orderList = new ArrayList<>();
        for (String menuId : orders.getMenuAmts().keySet()) {
            orderList.add(Order.builder()
                    .clientId(clientId)
                    .menuId(Integer.parseInt(menuId))
                    .amount(orders.getMenuAmts().get(menuId))
                    .status(Order.StatusOrder.ORDERED)
                    .build()
            );
        }
        return orderList;
    }

    public List<OrderDTO> createOrders(OrderReceiveDTO orders, int clientId) {
        List<Order> orderList = fromOrderReceiveDTO(orders, clientId);
        List<Order> createdOrders = orderDAO.createOrder(orderList);
        if (createdOrders == null) return null;
        return createdOrders.stream().map(mapper::toOrderDTO).toList();
    }

    public int updateOrderStatus(List<Integer> orderIds, String statusStr) {
        Optional<Order.StatusOrder> status = checkStatus(statusStr);
        if (status.isEmpty()) return -1;
        return orderDAO.updateOrderStatus(orderIds, status.get());
    }
}
