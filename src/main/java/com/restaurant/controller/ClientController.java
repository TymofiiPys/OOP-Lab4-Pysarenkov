package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.AuthToken;
import com.restaurant.dto.LoginDTO;
import com.restaurant.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "ClientServlet", value = "/auth")
public class ClientController extends HttpServlet {
    private final ClientService clientService = new ClientService();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginDTO loginDTO = objectMapper.readValue(
                req.getReader().lines().collect(Collectors.joining()),
                LoginDTO.class
        );
        String signup = req.getParameter("signup");
        if (signup != null){
            AuthToken token = clientService.signup(loginDTO);
            if (token.getStatus() < 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(token));
            }
        } else {
            if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            AuthToken token = clientService.authenticate(loginDTO);
            if (token.getStatus() < 0) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else if (token.getStatus() == 1) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString(token));
            }
        }
    }
}
