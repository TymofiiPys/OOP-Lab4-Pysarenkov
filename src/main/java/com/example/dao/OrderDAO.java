package com.example.dao;

import com.example.model.Client;
import com.example.model.Order;

import java.util.List;

public class OrderDAO {
    private final String getClientsUnpaidOrderQuery = "from Order o where o.clientId = :cl_id and o.status != StatusOrder.PAID";
    public List<Order> findAll() {

    }

    public List<Order> getClientsUnpaidOrder(Client client) {

    }
}
