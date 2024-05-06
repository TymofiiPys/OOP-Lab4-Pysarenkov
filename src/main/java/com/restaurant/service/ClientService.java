package com.restaurant.service;

import com.restaurant.dao.ClientDAO;
import com.restaurant.db.RestaurantDBConnection;

import java.sql.Connection;

public class ClientService {
    private final Connection conn = RestaurantDBConnection.getConnection();
    private final ClientDAO clientDAO = new ClientDAO();
}
