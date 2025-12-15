package com.microservice.identity.domain.port.in.auth;

public interface AuthenticateAndGenerateTokenUseCase {
    String authenticateAndGenerateToken(String username, String password);
}
