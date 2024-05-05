package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.OrderDisplayDTO;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "OrderServlet", value = "/orders")
public class OrderController extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<OrderDisplayDTO> orders = new ArrayList<>();
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

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(orders));
//        log.info("Parsed orders from DB");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<OrderReceiveDTO> orders = Arrays.asList(
                objectMapper.readValue(
                        req.getReader().lines().collect(Collectors.joining()),
                        OrderReceiveDTO[].class
                )
        );
        orderService.createOrders(orders);
        //TODO : logs and responses
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
