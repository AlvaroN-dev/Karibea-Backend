package com.microservice.identity.application.services;

import com.microservice.identity.domain.port.in.auth.*;
import org.springframework.stereotype.Service;

/**
 * Application Service that implements all authentication-related use case
 * interfaces.
 * This service acts as a facade that delegates to the actual use case
 * implementations,
 * providing a unified interface for the infrastructure layer (controllers).
 * 
 * Follows the Facade pattern and Dependency Inversion Principle.
 */
@Service
public class AuthApplicationService implements
        AuthenticateAndGenerateTokenUseCase,
        RefreshTokenUseCase,
        ValidateTokenUseCase,
        LogoutUseCase {

    private final AuthenticateAndGenerateTokenUseCase authenticateAndGenerateTokenUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthApplicationService(
            AuthenticateAndGenerateTokenUseCase authenticateAndGenerateTokenUseCase,
            RefreshTokenUseCase refreshTokenUseCase,
            ValidateTokenUseCase validateTokenUseCase,
            LogoutUseCase logoutUseCase) {
        this.authenticateAndGenerateTokenUseCase = authenticateAndGenerateTokenUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.validateTokenUseCase = validateTokenUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @Override
    public String authenticateAndGenerateToken(String username, String password) {
        return authenticateAndGenerateTokenUseCase.authenticateAndGenerateToken(username, password);
    }

    @Override
    public String refreshToken(String refreshToken) {
        return refreshTokenUseCase.refreshToken(refreshToken);
    }

    @Override
    public void validateToken(String token) {
        validateTokenUseCase.validateToken(token);
    }

    @Override
    public void logout(String token) {
        logoutUseCase.logout(token);
    }
}
