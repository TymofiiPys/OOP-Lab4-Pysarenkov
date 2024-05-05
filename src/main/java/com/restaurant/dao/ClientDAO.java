package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Client;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ClientDAO {

    private Connection connection;

    public ClientDAO() {
        this.connection = RestaurantDBConnection.getConnection();
    }

    public void createClient(Client client) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO clients (name) VALUES (?)");
            statement.setString(1, client.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when CREATING CLIENT " + client.toString() + ", stacktrace: ", e);
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
            log.error("SQLException when READING CLIENTS, stacktrace: ", e);
        }
        return clients;
    }

    public String getClientName(int clientId){
        String name = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT clients.name FROM clients WHERE clients.id = " + clientId);
            while (resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            log.error("SQLException when GETTING CLIENT'S ID ("
                    + clientId
                    + ") check DB for existing of client with this id, stacktrace: ", e);
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
            log.error("SQLException when UPDATING CLIENT ("
                    + client.toString()
                    + ") check DB for existing of client with this id, stacktrace: ", e);
        }
    }

    public void deleteClient(int clientId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM clients WHERE id = ?");
            statement.setInt(1, clientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when DELETING CLIENT WITH ID"
                    + clientId
                    + ") check DB for existing of client with this id, stacktrace: ", e);
        }
    }
}
