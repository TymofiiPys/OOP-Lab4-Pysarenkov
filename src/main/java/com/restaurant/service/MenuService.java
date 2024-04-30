package com.restaurant.service;

import com.restaurant.dao.MenuDAO;
import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Menu;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

public class MenuService {
    private final Connection conn = RestaurantDBConnection.getConnection();
    private final MenuDAO menuDAO = new MenuDAO(conn);

    public List<Menu> getMenu() {
//        log.info("Parsed menu from DB");
        return menuDAO.readMenu();

    }
}
