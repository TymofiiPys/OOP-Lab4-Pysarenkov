package com.example.dao;

import com.example.model.Client;
import com.example.model.Order;
import com.example.util.HibernateSessionFactory;

import java.util.List;

public class OrderDAO {
    private final String getClientsUnpaidOrderQuery = "from Order o where o.clientId = :cl_id and o.status != StatusOrder.PAID";
    public List<Order> findAll() {
        return (List<Order>) HibernateSessionFactory
                .getSessionFactory()
                .openSession()
                .createQuery("From Order", Order.class)
                .getResultList();
    }

    public List<Order> getClientsUnpaidOrder(Client client) {
        return (List<Order>) HibernateSessionFactory
                .getSessionFactory()
                .openSession()
                .createQuery(getClientsUnpaidOrderQuery, Order.class)
                .setParameter("cl_id", client.getId())
                .getResultList();
    }
}
