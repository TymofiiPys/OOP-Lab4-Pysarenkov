package com.example.controller;

import com.example.dao.MenuDAO;
import com.example.dao.OrderDAO;
import com.example.model.Menu;
import com.example.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="RestaurantServlet", value = "/")
public class RestaurantController extends HttpServlet {

    private MenuDAO menuDAO;
    private OrderDAO orderDAO;

    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "password");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        menuDAO = new MenuDAO(conn);
        orderDAO = new OrderDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/menu":
                getMenu(request, response);
                break;
            case "/listUnpaidOrders":
                listUnpaidOrders(request, response);
                break;
            default:
                        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "message" + "</h1>");
        out.println("</body></html>");
        }


    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Menu> menu = menuDAO.readMenu();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray();

        for (Menu menuItem : menu) {
            JSONObject jsonMenuItem = new JSONObject();
            jsonMenuItem.put("id", menuItem.getId());
            jsonMenuItem.put("name", menuItem.getName());
            jsonMenuItem.put("meal", menuItem.isMealOrDrink());
            jsonMenuItem.put("cost", menuItem.getCost());
            jsonArray.put(jsonMenuItem);
        }

        response.setContentType("application/json");
        out.write(jsonArray.toString());
    }

    private void listUnpaidOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> unpaidOrders = orderDAO.getUnpaidOrders();
        request.setAttribute("unpaidOrders", unpaidOrders);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/unpaidOrders.jsp");
        dispatcher.forward(request, response);
    }
}
