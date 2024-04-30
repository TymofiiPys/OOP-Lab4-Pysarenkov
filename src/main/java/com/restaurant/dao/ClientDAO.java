package com.restaurant.dao;

import com.restaurant.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    public void createClient(Client client) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO clients (name) VALUES (?)");
            statement.setString(1, client.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> readClients() {
        List<Client> clients = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            while (resultSet.next()) {
                Client client = Client.builder().build();
                client.setId(resultSet.getInt("id"));
                client.setName(resultSet.getString("name"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public String getClientName(int id){
        String name = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT clients.name FROM clients WHERE clients.id = " + id);
            while (resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void updateClient(Client client) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE clients SET name = ? WHERE id = ?");
            statement.setString(1, client.getName());
            statement.setInt(2, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(int clientId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM clients WHERE id = ?");
            statement.setInt(1, clientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
