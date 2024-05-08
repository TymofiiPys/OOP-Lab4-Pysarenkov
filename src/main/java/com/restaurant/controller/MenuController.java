package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.MenuCreateDTO;
import com.restaurant.dto.MenuDTO;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.model.Client;
import com.restaurant.model.Menu;
import com.restaurant.service.MenuService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "MenuServlet", value = "/menu")
public class MenuController extends HttpServlet {
    private final MenuService menuService = new MenuService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<MenuDTO> menu = menuService.getMenu();
        resp.setContentType("application/json");
        if (menu == null)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(menu));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Client client = objectMapper.readValue(
                req.getAttribute("client").toString(),
                Client.class
        );
        if(!client.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        MenuCreateDTO menuToCreate = objectMapper.readValue(
                req.getReader().lines().collect(Collectors.joining()),
                MenuCreateDTO.class
        );
        MenuDTO createdMenu = menuService.createMenu(menuToCreate);
        if (createdMenu == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(createdMenu));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Client client = objectMapper.readValue(
                req.getAttribute("client").toString(),
                Client.class
        );
        if(!client.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        MenuDTO menuToUpdate = objectMapper.readValue(
                req.getReader().lines().collect(Collectors.joining()),
                MenuDTO.class
        );
        int updatedRows = menuService.updateMenu(menuToUpdate);
        if (updatedRows < 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (updatedRows == 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Client client = objectMapper.readValue(
                req.getAttribute("client").toString(),
                Client.class
        );
        if(!client.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        int menuIdToDelete = objectMapper.readValue(
                req.getReader().lines().collect(Collectors.joining()),
                Integer.class
        );
        int deletedRows = menuService.deleteMenu(menuIdToDelete);
        if (deletedRows < 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (deletedRows == 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
