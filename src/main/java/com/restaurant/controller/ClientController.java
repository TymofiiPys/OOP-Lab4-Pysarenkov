package com.restaurant.controller;

import com.restaurant.db.RestaurantDBConnection;

import java.sql.Connection;

public class ClientController {
    private final Connection conn = RestaurantDBConnection.getConnection();
}
