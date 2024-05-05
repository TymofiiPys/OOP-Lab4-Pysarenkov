package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.dto.PaymentDTO;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<PaymentDTO> payments = Arrays.asList(
                objectMapper.readValue(
                        req.getReader().lines().collect(Collectors.joining()),
                        PaymentDTO[].class
                )
        );
        paymentService.createPayment(payments);
    }
}
