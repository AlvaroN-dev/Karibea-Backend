package com.microservice.identity.application.usecases.auth;

import com.microservice.identity.domain.port.in.auth.ValidateTokenUseCase;
import com.microservice.identity.domain.port.out.TokenServicePort;

public class ValidateTokenUseCaseImpl implements ValidateTokenUseCase {

    private final TokenServicePort tokenServicePort;

    public ValidateTokenUseCaseImpl(TokenServicePort tokenServicePort) {
        this.tokenServicePort = tokenServicePort;
    }

    @Override
    public void validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        if (!tokenServicePort.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }
}
