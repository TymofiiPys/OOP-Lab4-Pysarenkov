package com.restaurant.controller;

import com.restaurant.db.RestaurantDBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.sql.Connection;
@WebServlet(name = "ClientServlet", value = "/user")
public class ClientController extends HttpServlet {
}
