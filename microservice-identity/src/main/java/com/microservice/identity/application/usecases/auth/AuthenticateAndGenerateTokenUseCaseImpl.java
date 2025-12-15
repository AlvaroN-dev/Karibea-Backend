package com.microservice.identity.application.usecases.auth;

import com.microservice.identity.application.exception.ValidationException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.auth.AuthenticateAndGenerateTokenUseCase;
import com.microservice.identity.domain.port.out.PasswordEncoderPort;
import com.microservice.identity.domain.port.out.TokenServicePort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

public class AuthenticateAndGenerateTokenUseCaseImpl implements AuthenticateAndGenerateTokenUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenServicePort tokenServicePort;

    public AuthenticateAndGenerateTokenUseCaseImpl(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenServicePort tokenServicePort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenServicePort = tokenServicePort;
    }

    @Override
    public String authenticateAndGenerateToken(String username, String password) {
        validateInput(username, password);

        // Find user by username
        // Use generic error message to avoid revealing if username exists (security
        // best practice)
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Invalid credentials"));

        // Verify password matches
        // Use same generic error message for wrong password (security best practice)
        if (!passwordEncoderPort.matches(password, user.getPasswordHash())) {
            throw new ValidationException("Invalid credentials");
        }

        // Verify user account is not deleted
        if (user.isDeleted()) {
            throw new IllegalStateException("User account has been deleted");
        }

        // Verify user account is enabled
        if (!user.isEnabled()) {
            throw new IllegalStateException("User account is disabled");
        }


        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email not verified. Please verify your email before logging in.");
        }

        // Update last login timestamp
        user.updateLastLogin();
        userRepositoryPort.save(user);

        // Generate and return JWT token
        return tokenServicePort.generateToken(user);
    }

    private void validateInput(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
