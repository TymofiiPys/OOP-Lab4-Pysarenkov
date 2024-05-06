package com.restaurant.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dao.ClientDAO;
import com.restaurant.dao.MenuDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.PaymentDAO;
import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Menu;
import com.restaurant.model.Order;
import com.restaurant.model.Payment;
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
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "RestaurantServlet", value = "/old")
public class RestaurantController extends HttpServlet {

    private MenuDAO menuDAO;
    private OrderDAO orderDAO;
    private ClientDAO clientDAO;
    private PaymentDAO paymentDAO;
    private Connection conn = RestaurantDBConnection.getConnection();
    private final Logger log = LogManager.getRootLogger();

    @Override
    public void init() throws ServletException {
        menuDAO = new MenuDAO();
        orderDAO = new OrderDAO();
        paymentDAO = new PaymentDAO();
        clientDAO = new ClientDAO();
        log.trace("Configuration File Defined To Be :: " + System.getProperty("log4j.configurationFile"));

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "message" + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void destroy() {
        super.destroy();
//        try {
//            conn.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse JSON request body to extract order data

//        OrderDTO[] orders = new ObjectMapper().readValue(request.getReader().lines().collect(Collectors.joining()), OrderDTO[].class);

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
            orders.add(Order.builder()
                    .clientId(clientId)
                    .menuId(key)
                    .amount(menuItemAmounts.get(key))
                    .status(Order.StatusOrder.ORDERED)
                    .build());
//            orders.add(new Order(clientId, key, menuItemAmounts.get(key), Order.StatusOrder.ORDERED));
        }

        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        orderDAO.createOrder(orders);
        jsonResponse.put("message", "Database error");
        response.getWriter().write(jsonResponse.toString());
        log.error("DB error");

        jsonResponse.put("message", "Order submitted successfully");
        log.info("DB insertion successful");
        response.getWriter().write(jsonResponse.toString());
    }

    private void getOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Order> orders = orderDAO.readOrders(null, null);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray();

        for (Order order : orders) {
            JSONObject jsonMenuItem = new JSONObject();
            Menu item = menuDAO.getMenuItem(order.getMenuId());
            jsonMenuItem.put("id", order.getId());
            jsonMenuItem.put("client_id", order.getClientId());
            jsonMenuItem.put("client_name", clientDAO.getClientName(order.getClientId()));
            jsonMenuItem.put("menu_item", item.getName());
            jsonMenuItem.put("amount", order.getAmount());
            jsonMenuItem.put("status", order.getStatus().toString());
            jsonMenuItem.put("cost", item.getCost());
            jsonArray.put(jsonMenuItem);
        }

        response.setContentType("application/json");
        out.write(jsonArray.toString());


    }

    private void getUnpaidOrders(HttpServletRequest request, HttpServletResponse response, int clientId) throws IOException {
        List<Order> orders = orderDAO.readOrders(null, null);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray();

        for (Order order : orders) {
            JSONObject jsonMenuItem = new JSONObject();
            jsonMenuItem.put("id", order.getId());
            jsonMenuItem.put("client_id", clientId);
//            jsonMenuItem.put("client_name", clientDAO.getClientName(order.getClientId()));
            Menu item = menuDAO.getMenuItem(order.getMenuId());
            jsonMenuItem.put("menu_item", item.getName());
            jsonMenuItem.put("menu_id", order.getMenuId());
            jsonMenuItem.put("amount", order.getAmount());
            jsonMenuItem.put("status", order.getStatus().toString());
            jsonMenuItem.put("cost", item.getCost());
            jsonArray.put(jsonMenuItem);
        }

        response.setContentType("application/json");
        out.write(jsonArray.toString());

        log.info("Parsed orders from DB");
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONArray jsonArray = new JSONArray(request.getReader().lines().collect(Collectors.joining()));

        log.info("Received request for order status update \n " + jsonArray.toString());

        List<Integer> orderIds = new ArrayList<>();
        Order.StatusOrder statusOrder = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject order = jsonArray.getJSONObject(i);
            int orderId = order.getInt("id");
            if (statusOrder == null) {
                statusOrder = order.get("status").equals("ORDERED") ?
                        Order.StatusOrder.ISSUED_FOR_PAYMENT :
                        Order.StatusOrder.PAID;
            }
            orderIds.add(orderId);
        }

        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        orderDAO.updateOrderStatus(orderIds, statusOrder);
        jsonResponse.put("message", "Database error");
        response.getWriter().write(jsonResponse.toString());
        log.error("DB error");

        jsonResponse.put("message", "Order status updated successfully");
        log.info("DB update successful");
        response.getWriter().write(jsonResponse.toString());
    }

    private void createPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONArray jsonArray = new JSONArray(request.getReader().lines().collect(Collectors.joining()));

        log.info("Payment received: \n " + jsonArray.toString());

        int clientId = jsonArray.getJSONObject(0).getInt("client_id");
        double totalCost = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject order = jsonArray.getJSONObject(i);
            int orderId = order.getInt("id");
            totalCost += order.getDouble("cost") * order.getInt("amount");
        }

        Payment payment = Payment.builder()
                .cost(totalCost)
                .clientId(clientId)
                .time(new Timestamp(new Date().getTime()))
                .build();

        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        paymentDAO.createPayment(payment);
        jsonResponse.put("message", "Database error");
        response.getWriter().write(jsonResponse.toString());
        log.error("DB error");

        jsonResponse.put("message", "Order submitted successfully");
        log.info("DB insertion successful");
        response.getWriter().write(jsonResponse.toString());
    }
}
