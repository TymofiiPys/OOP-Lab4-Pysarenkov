package com.restaurant.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.OrderDisplayDTO;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.model.Client;
import com.restaurant.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "OrderServlet", value = "/orders")
public class OrderController extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<OrderDisplayDTO> orders;
        if (req.getParameter("which") == null)
            orders = orderService.getAllOrders();
        else {
            if (req.getParameter("clid") == null)
                orders = orderService.getOrdersFilteredByStatus(req.getParameter("which"));
            else
                orders = orderService.getOrdersFilteredByStatusAndId(
                        req.getParameter("which"),
                        Integer.parseInt(req.getParameter("clid"))
                );
        }
        if (orders == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (orders.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(orders));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int clientId = ((Client) req.getAttribute("client")).getId();
        OrderReceiveDTO orders = new OrderReceiveDTO();
        orders.setMenuAmts(
                objectMapper.readValue(
                        req.getReader().lines().collect(Collectors.joining()),
                        new TypeReference<>() {
                        }
                )
        );
//        List<OrderReceiveDTO> orders = Arrays.asList(
//                objectMapper.readValue(
//                        req.getReader().lines().collect(Collectors.joining()),
//                        OrderReceiveDTO[].class
//                )
//        );
//        List<OrderDTO> createdOrders = orderService.createOrders(orders);
//        if (createdOrders == null) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        } else {
//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.getWriter().write(objectMapper.writeValueAsString(createdOrders));
//        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Integer> orderIdsToIssue = Arrays.asList(
                objectMapper.readValue(
                        req.getReader().lines().collect(Collectors.joining()),
                        Integer[].class
                )
        );
        if (req.getParameter("status").equals("ISSUED_FOR_PAYMENT")) {
            Client client = objectMapper.readValue(
                    req.getAttribute("client").toString(),
                    Client.class
            );
            if (!client.isAdmin()) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        int updatedRows = orderService.updateOrderStatus(orderIdsToIssue, req.getParameter("status"));
        if (updatedRows < 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (updatedRows == 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
