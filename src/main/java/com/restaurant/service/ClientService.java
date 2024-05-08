package com.restaurant.service;

import com.restaurant.dao.ClientDAO;
import com.restaurant.dto.LoginDTO;

public class ClientService {
    private final ClientDAO clientDAO = new ClientDAO();

    public boolean authenticate(LoginDTO loginDTO) {
        DaoManager mgr = new DaoManager();
        Connection conn = mgr.getConnection();
        UserDao dao = new UserDao(conn);
        User user = null;
        Password pass = null;
        try {
            user = (User) dao.readByEmail(email);
            if(user == null){
                throw new Exception();
            }
            pass = dao.getPassword(user.getId());
            String hashedPass = BCrypt.hashpw(pwd, pass.getSalt());
            if(!hashedPass.equals(pass.getPassword())){
                throw new Exception();
            }
        } catch (Exception e) {
            resp.getWriter().println("[]");
            return;
        }

        Algorithm algorithm = Algorithm.HMAC256("baeldung");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Baeldung")
                .build();
        String jwt = JWT.create()
                .withIssuer("Baeldung")
                .withClaim("id", user.getId())
                .withClaim("login", user.getLogin())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole())
                .sign(algorithm);

        Token token = new Token(jwt);
        try {
            String userJson = JsonParser.toJsonObject(token);
            resp.getWriter().println(userJson);
        } catch (Exception e) {
            resp.getWriter().println("[]");
        }
    }
}
