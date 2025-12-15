package com.microservice.identity.application.usecases.user;

import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.AuthenticateUserUseCase;
import com.microservice.identity.domain.port.out.PasswordEncoderPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public AuthenticateUserUseCaseImpl(UserRepositoryPort userRepositoryPort, PasswordEncoderPort passwordEncoderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoderPort.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new IllegalStateException("User account is disabled");
        }

        user.updateLastLogin();
        return userRepositoryPort.save(user);
    }

}
