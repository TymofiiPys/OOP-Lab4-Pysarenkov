package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Client;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class ClientDAO {

    private Connection connection;

    public ClientDAO() {
        this.connection = RestaurantDBConnection.getConnection();
    }

    public Optional<Client> createClient(Client client) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO clients (name) VALUES (?)");
            statement.setString(1, client.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when CREATING CLIENT " + client.toString() + ", stacktrace: ", e);
            return Optional.empty();
        }
        return Optional.of(client);
    }

    public Optional<List<Client>> readClients() {
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
            return Optional.empty();
        }
        return Optional.of(clients);
    }

    public Optional<String> getClientName(int clientId) {
        try {
            String name = "";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT clients.name FROM clients WHERE clients.id = " + clientId);
            while (resultSet.next()) {
                name = resultSet.getString("name");
            }
            if (name.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(name);
        } catch (SQLException e) {
            log.error("SQLException when READING CLIENT with ID ("
                    + clientId
                    + "), stacktrace: ", e);
        }
        return Optional.empty();
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
                    + "), stacktrace: ", e);
        }
    }

    public void deleteClient(int clientId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM clients WHERE id = ?");
            statement.setInt(1, clientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when DELETING CLIENT with ID"
                    + clientId
                    + "), stacktrace: ", e);
        }
    }
}
