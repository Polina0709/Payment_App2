package com.flowbank.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final String SECRET = "secret";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String validateTokenAndGetSubject(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            List<String> roles = jwt.getClaim("roles").asList(String.class);
            if (roles == null) return List.of();

            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public String generateToken(String subject, String role) {
        return JWT.create()
                .withSubject(subject)
                .withClaim("roles", List.of("ROLE_" + role.toUpperCase()))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .sign(algorithm);
    }
}
