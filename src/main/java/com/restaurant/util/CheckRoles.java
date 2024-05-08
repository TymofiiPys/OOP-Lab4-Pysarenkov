package com.restaurant.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;

public class CheckRoles {
//    public static Client getClient(HttpServletRequest req) {
//        String token = req.getHeader("access-token");
//        Algorithm algorithm = Algorithm.HMAC256("baeldung");
//        JWTVerifier verifier = JWT.require(algorithm)
//                .withIssuer("Baeldung")
//                .build();
//        DecodedJWT decodedJWT = verifier.verify(token);
//        return decodedJWT.getClaim("isAdmin").asBoolean();
//    }
}
