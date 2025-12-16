package com.microservice.identity.domain.port.out;

import com.microservice.identity.domain.models.User;

import java.util.UUID;

public interface TokenServicePort {
    String generateToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    UUID getUserIdFromToken(String token);
}
