package com.example.dao;

import com.example.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private Connection connection;

    // Constructor to initialize the database connection
    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"Clients\"");
            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getInt("ID"));
                client.setName(resultSet.getString("Name"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
