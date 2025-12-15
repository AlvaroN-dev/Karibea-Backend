package com.microservice.identity.application.usecases.auth;

import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.auth.RefreshTokenUseCase;
import com.microservice.identity.domain.port.out.TokenServicePort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final TokenServicePort tokenServicePort;
    private final UserRepositoryPort userRepositoryPort;

    public RefreshTokenUseCaseImpl(TokenServicePort tokenServicePort, UserRepositoryPort userRepositoryPort) {
        this.tokenServicePort = tokenServicePort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public String refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }

        // Validate the refresh token
        if (!tokenServicePort.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        // Extract user ID from token
        UUID userId = tokenServicePort.getUserIdFromToken(refreshToken);

        // Find user to ensure they still exist and are active
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Verify user is still enabled and not deleted
        if (user.isDeleted()) {
            throw new IllegalStateException("User account has been deleted");
        }

        if (!user.isEnabled()) {
            throw new IllegalStateException("User account is disabled");
        }

        // Generate new access token
        return tokenServicePort.generateToken(user);
    }
}
