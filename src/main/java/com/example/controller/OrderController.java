package com.example.controller;

import com.example.dao.OrderDAO;
import com.example.model.Client;
import com.example.model.Menu;
import com.example.model.Order;

import java.util.HashMap;
import java.util.List;

public class OrderController {
    public void createOrder(HashMap<Menu, Integer> chosenMenuItems) {

    }

    public List<Order> getAllOrders() {
        return new OrderDAO().findAll();
    }

    public void issueOrder(Client client) {
        List<Order> unpaidOrder = new OrderDAO().getClientsUnpaidOrder(client);
    }
}
