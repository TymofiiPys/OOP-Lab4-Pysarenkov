package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.MenuDTO;
import com.restaurant.model.Menu;
import com.restaurant.service.MenuService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "MenuServlet", value = "/menu")
public class MenuController extends HttpServlet {
    private final MenuService menuService = new MenuService();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MenuDTO> menu = menuService.getMenu();
        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//        JSONArray jsonArray = new JSONArray();
//
//        for (Menu menuItem : menu) {
//            JSONObject jsonMenuItem = new JSONObject();
//            jsonMenuItem.put("id", menuItem.getId());
//            jsonMenuItem.put("name", menuItem.getName());
//            jsonMenuItem.put("meal", menuItem.isMealOrDrink());
//            jsonMenuItem.put("cost", menuItem.getCost());
//            jsonArray.put(jsonMenuItem);
//        }

        resp.getWriter().write(objectMapper.writeValueAsString(menu));
    }
}
