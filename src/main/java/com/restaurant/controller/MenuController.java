package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.MenuDTO;
import com.restaurant.service.MenuService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MenuServlet", value = "/menu")
public class MenuController extends HttpServlet {
    private final MenuService menuService = new MenuService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<MenuDTO> menu = menuService.getMenu();
        resp.setContentType("application/json");
        if(menu == null)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(menu));
        }
    }
}
