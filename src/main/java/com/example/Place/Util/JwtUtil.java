package com.example.Place.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate Token
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())           // algorithm auto-selected
                .compact();
    }

    // Extract Email
    public String extractEmail(String token) {
        return parseToken(token).getSubject();
    }

    // Validate Token
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // New way f
    // $2a$10$yz8qnIes0dr41ccEDZJFGuiA6zXIcT7NpbFrjiC7V8aj1yygClx6Wor jjwt 0.12+
    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // <-- this replaces setSigningKey()
                .build()
                .parseSignedClaims(token)
                .getPayload();                 // <-- payload = Claims
    }
}