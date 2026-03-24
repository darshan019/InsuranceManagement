package com.internship.InsuranceManagement.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "THIS_IS_MY_SECRET_JWT_KEY_12345678901234567890"; // must be 32+ chars
    private final long EXPIRATION = 3600000; // 1 hour

    // Generate token with email + role
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)                 // this is email
                .claim("role", role)               // USER or ADMIN
                .setIssuedAt(new Date())           // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // expiry
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes())) // signature
                .compact();
    }

    // Extract ALL claims
    public Claims extractAll(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract email stored in "sub"
    public String extractEmail(String token) {
        return extractAll(token).getSubject();
    }

    // Extract role
    public String extractRole(String token) {
        return extractAll(token).get("role", String.class);
    }

    // Validate token expiration
    public boolean isValid(String token) {
        return extractAll(token).getExpiration().after(new Date());
    }
}