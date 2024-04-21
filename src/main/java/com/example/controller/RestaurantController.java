package com.example.controller;

import com.example.dao.MenuDAO;
import com.example.dao.OrderDAO;
import com.example.model.Menu;
import com.example.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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
            case "/listMenus":
                listMenus(request, response);
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

    private void listMenus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Menu> menus = menuDAO.getAllMenus();
        request.setAttribute("menus", menus);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/menu.jsp");
        dispatcher.forward(request, response);
    }

    private void listUnpaidOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> unpaidOrders = orderDAO.getUnpaidOrders();
        request.setAttribute("unpaidOrders", unpaidOrders);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/unpaidOrders.jsp");
        dispatcher.forward(request, response);
    }
}
