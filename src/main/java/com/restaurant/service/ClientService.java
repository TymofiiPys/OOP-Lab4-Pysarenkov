package com.restaurant.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.restaurant.dao.ClientDAO;
import com.restaurant.dto.AuthToken;
import com.restaurant.dto.LoginDTO;
import com.restaurant.model.Client;
import com.restaurant.model.Password;
import org.mindrot.jbcrypt.BCrypt;

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
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("IMBARESTAURANT")
                .build();
        String jwt = JWT.create()
                .withIssuer("IMBARESTAURANT")
                .withClaim("id", clientToAuth.getId())
                .withClaim("email", clientToAuth.getEmail())
                .withClaim("isAdmin", clientToAuth.isAdmin())
                .sign(algorithm);
        return new AuthToken(0, jwt);
    }
}
