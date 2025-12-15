package com.microservice.identity.application.usecases.auth;

import com.microservice.identity.domain.port.in.auth.LogoutUseCase;

public class LogoutUseCaseImpl implements LogoutUseCase {

    public LogoutUseCaseImpl() {
        // No dependencies needed for current implementation
    }

    @Override
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        // Note: Current implementation is a no-op since TokenServicePort doesn't have
        // an invalidateToken method. In a production system, you would typically:
        // 1. Add the token to a blacklist/revocation list
        // 2. Store revoked tokens in Redis or database
        // 3. Check blacklist during token validation
        //
        // For now, tokens will expire naturally based on their expiration time.
        // The client should discard the token on logout.
    }
}
