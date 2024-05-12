package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.PaymentCreateDTO;
import com.restaurant.dto.PaymentDisplayDTO;
import com.restaurant.model.Client;
import com.restaurant.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "PaymentServlet", value = "/payment")
public class PaymentController extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Client client = objectMapper.readValue(
                req.getAttribute("client").toString(),
                Client.class
        );
        if(!client.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        List<PaymentDisplayDTO> payments = paymentService.getAllPayments();
        resp.setContentType("application/json");
        if (payments == null)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(payments));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<PaymentCreateDTO> payments = Arrays.asList(
                objectMapper.readValue(
                        req.getReader().lines().collect(Collectors.joining()),
                        PaymentCreateDTO[].class
                )
        );
        PaymentCreateDTO createdPayment = paymentService.createPayment(payments);
        resp.setContentType("application/json");
        if(createdPayment == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(createdPayment));
        }
    }
}
