package com.example.controller;

import com.example.dao.MenuDAO;
import com.example.dao.OrderDAO;
import com.example.model.Menu;
import com.example.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "RestaurantServlet", value = "/")
public class RestaurantController extends HttpServlet {

    private MenuDAO menuDAO;
    private OrderDAO orderDAO;
    private Connection conn;
    private Logger log = LogManager.getRootLogger();
//    private Logger log = LogManager.getLogger(RestaurantController.class);


    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "password");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        menuDAO = new MenuDAO(conn);
        orderDAO = new OrderDAO(conn);
        log.trace("Configuration File Defined To Be :: " + System.getProperty("log4j.configurationFile"));

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/menu":
                getMenu(request, response);
                break;
            case "/listUnpaidOrders":
                listUnpaidOrders(request, response);
                break;
            default:
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><body>");
                out.println("<h1>" + "message" + "</h1>");
                out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/orders":
                createOrder(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Menu> menu = menuDAO.readMenu();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray();

        for (Menu menuItem : menu) {
            JSONObject jsonMenuItem = new JSONObject();
            jsonMenuItem.put("id", menuItem.getId());
            jsonMenuItem.put("name", menuItem.getName());
            jsonMenuItem.put("meal", menuItem.isMealOrDrink());
            jsonMenuItem.put("cost", menuItem.getCost());
            jsonArray.put(jsonMenuItem);
        }

        response.setContentType("application/json");
        out.write(jsonArray.toString());

        log.info("Parsed menu from DB");
    }

    private void listUnpaidOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> unpaidOrders = orderDAO.getUnpaidOrders();
        request.setAttribute("unpaidOrders", unpaidOrders);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/unpaidOrders.jsp");
        dispatcher.forward(request, response);
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse JSON request body to extract order data
        JSONObject jsonBody = new JSONObject(request.getReader().lines().collect(Collectors.joining()));

        log.info("Order received: \n " + jsonBody.toString());

        Iterator<String> keys = jsonBody.keys();
        int clientId = -1;
        HashMap<Integer, Integer> menuItemAmounts = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.equals("client")) {
                clientId = jsonBody.getInt(key);
            } else {
                int menuId = Integer.parseInt(key);
                int amt = jsonBody.getInt(key);
                menuItemAmounts.put(menuId, amt);
            }
        }

        List<Order> orders = new ArrayList<>();
        for (Integer key : menuItemAmounts.keySet()) {
            orders.add(new Order(clientId, key, menuItemAmounts.get(key), Order.StatusOrder.ORDERED));
        }

        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        try {
            orderDAO.createOrder(orders);
        } catch (SQLException e) {
            jsonResponse.put("message", "Database error");
            response.getWriter().write(jsonResponse.toString());
            log.error("DB error");
            return;
        }

        jsonResponse.put("message", "Order submitted successfully");
        log.info("DB insertion successful");
        response.getWriter().write(jsonResponse.toString());
    }
}
