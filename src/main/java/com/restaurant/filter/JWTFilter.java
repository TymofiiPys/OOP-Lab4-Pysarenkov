package com.restaurant.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.model.Client;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;


@WebFilter(asyncSupported = true, urlPatterns = {"/menu", "/orders", "/payment"})
public class JWTFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("access-token");
        Algorithm algorithm = Algorithm.HMAC256("secretlysecret");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("IMBARESTAURANT")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        int id = decodedJWT.getClaim("id").asInt();
        String email = decodedJWT.getClaim("email").asString();
        boolean isAdmin = decodedJWT.getClaim("isAdmin").asBoolean();
        Client client = Client.builder()
                .id(id)
                .email(email)
                .isAdmin(isAdmin)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String clientJSON = objectMapper.writeValueAsString(client);
        request.setAttribute("client", clientJSON);
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
    }
}