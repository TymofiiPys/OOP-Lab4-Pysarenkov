package com.restaurant.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.restaurant.dao.ClientDAO;
import com.restaurant.dto.AuthToken;
import com.restaurant.dto.LoginDTO;
import com.restaurant.model.Client;
import com.restaurant.model.Password;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;

public class ClientService {
    private final ClientDAO clientDAO = new ClientDAO();

    public AuthToken authenticate(LoginDTO loginDTO) {
        Client clientToAuth = clientDAO.readByEmail(loginDTO.getEmail());
        if (clientToAuth == null) {
            return new AuthToken(-1, "");
        }
        Password password = clientDAO.getPassword(clientToAuth.getId());
        String hashedPwdLogin = BCrypt.hashpw(loginDTO.getPassword(), password.getSalt());
        if(!hashedPwdLogin.equals(password.getHash())) {
            return new AuthToken(1, "");
        }

        Algorithm algorithm = Algorithm.HMAC256("secretlysecret");
        String jwt = JWT.create()
                .withIssuer("IMBARESTAURANT")
                .withClaim("id", clientToAuth.getId())
                .withClaim("email", clientToAuth.getEmail())
                .withClaim("isAdmin", clientToAuth.isAdmin())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000L))
                .sign(algorithm);
        return new AuthToken(0, jwt);
    }

    public AuthToken signup(LoginDTO loginDTO) {
        Client createdClient = clientDAO.createClient(Client.builder().email(loginDTO.getEmail()).isAdmin(false).build(),
                loginDTO.getPassword());
        if (createdClient == null) {
            return new AuthToken(-1, "");
        }

        Algorithm algorithm = Algorithm.HMAC256("secretlysecret");
        String jwt = JWT.create()
                .withIssuer("IMBARESTAURANT")
                .withClaim("id", createdClient.getId())
                .withClaim("email", createdClient.getEmail())
                .withClaim("isAdmin", createdClient.isAdmin())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000L))
                .sign(algorithm);
        return new AuthToken(0, jwt);
    }
}
