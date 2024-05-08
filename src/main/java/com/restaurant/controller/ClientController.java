package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.LoginDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "ClientServlet", value = "/auth")
public class ClientController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pwd = req.getParameter("password");
        String email = req.getParameter("email");
        LoginDTO loginDTO = objectMapper.readValue(
                req.getReader().lines().collect(Collectors.joining()),
                LoginDTO.class
        );
        if(loginDTO.getEmail() == null || loginDTO.getPassword() == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

    }
}
