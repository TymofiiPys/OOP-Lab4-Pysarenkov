package com.restaurant.dao;

import com.restaurant.db.RestaurantDBConnection;
import com.restaurant.model.Client;
import com.restaurant.model.Password;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class ClientDAO {

    private final Connection connection;

    public ClientDAO() {
        this.connection = RestaurantDBConnection.getConnection();
    }

    public Client createClient(Client client, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO clients (email, password, salt, is_admin) VALUES (?, ?, ?, ?)");
            statement.setString(1, client.getEmail());
            String salt = BCrypt.gensalt();
            String hash = BCrypt.hashpw(password, salt);
            statement.setString(2, hash);
            statement.setString(3, salt);
            statement.setBoolean(4, client.isAdmin());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException when CREATING CLIENT " + client.toString() + ", stacktrace: ", e);
            return null;
        }
        return client;
    }

    public List<Client> readClients() {
        List<Client> clients = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            while (resultSet.next()) {
                Client client = Client.builder().build();
                client.setId(resultSet.getInt("id"));
                client.setEmail(resultSet.getString("email"));
                clients.add(client);
            }
        } catch (SQLException e) {
            log.error("SQLException when READING CLIENTS, stacktrace: ", e);
            return null;
        }
        return clients;
    }

    public Optional<String> getClientEmail(int clientId) {
        try {
            String name = "";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT clients.name FROM clients WHERE clients.id = " + clientId);
            while (resultSet.next()) {
                name = resultSet.getString("email");
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

    public Client readByEmail(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM clients WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Client client = Client.builder()
                        .id(resultSet.getInt("id"))
                        .email(email)
                        .isAdmin(resultSet.getBoolean("is_admin"))
                        .build();
                return client;
            }
        } catch (SQLException e) {
            log.error("SQLException when READING CLIENT with EMAIL ("
                    + email
                    + "), stacktrace: ", e);
        }
        return null;
    }

    public void updateClient(Client client) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE clients SET email = ? WHERE id = ?");
            statement.setString(1, client.getEmail());
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

    public Password getPassword(int clientId) {
        try {
            String sql = "SELECT password, salt FROM clients WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Password.builder()
                        .hash(resultSet.getString("password"))
                        .salt(resultSet.getString("salt"))
                        .build();
            }
        } catch (SQLException e) {
            log.error("SQLException when READING PASSWORD of CLIENT with ID"
                    + clientId
                    + "), stacktrace: ", e);
        }
        return null;
    }
}
