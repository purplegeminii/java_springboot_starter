package com.example.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Read secret key from application.properties
    @Value("${jwt.secret}")
    private String secret;

    // Read expiry from application.properties
    @Value("${jwt.expiration}")
    private long expiration;

    // Generate a token for a given username and role
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Extract the username from a token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract the role from a token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // Check the token is valid and not expired
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token); // throws if invalid or expired
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}