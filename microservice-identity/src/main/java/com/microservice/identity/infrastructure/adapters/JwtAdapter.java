package com.microservice.identity.infrastructure.adapters;

import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.out.TokenServicePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JWT Adapter for token generation and validation.
 * Implements TokenServicePort using JJWT library (version 0.13.0).
 * Follows hexagonal architecture by adapting JWT to domain.
 */
@Component
public class JwtAdapter implements TokenServicePort {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_EMAIL = "email";


    private final SecretKey secretKey;
    private final long jwtExpiration;
    private final long refreshExpiration;

    public JwtAdapter(
            @Value("${jwt.secret:your-256-bit-secret-key-change-this-in-production-please-make-it-secure}") String secret,
            @Value("${jwt.expiration:3600000}") long jwtExpiration,
            @Value("${jwt.refresh-expiration:86400000}") long refreshExpiration) {
        // Create SecretKey from string for JJWT 0.13.0
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, user.getId().toString());
        claims.put(CLAIM_USERNAME, user.getUsername());
        claims.put(CLAIM_EMAIL, user.getEmail());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, user.getId().toString());
        claims.put("type", "refresh");

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    @Override
    public UUID getUserIdFromToken(String token) {
        Claims claims = extractClaims(token);
        String userIdStr = claims.get(CLAIM_USER_ID, String.class);
        return UUID.fromString(userIdStr);
    }

    /**
     * Helper method to extract claims from token.
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
