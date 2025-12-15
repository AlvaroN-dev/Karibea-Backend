package com.microservice.identity.application.usecases.user;

import com.microservice.identity.application.exception.ValidationException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.ChangePasswordUseCase;
import com.microservice.identity.domain.port.out.PasswordEncoderPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class ChangePasswordUseCaseImpl implements ChangePasswordUseCase {

    private final UserRepositoryPort  userRepositoryPort;
    private final PasswordEncoderPort  passwordEncoderPort;

    public ChangePasswordUseCaseImpl(UserRepositoryPort userRepositoryPort, PasswordEncoderPort passwordEncoderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ValidationException("User not found"));

        if (!passwordEncoderPort.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (newPassword.length() < 8) {
            throw new ValidationException("newPassword", "Password must be at least 8 characters");
        }

        String hashedPassword = passwordEncoderPort.encode(newPassword);
        user.changePasswordHash(hashedPassword);
        userRepositoryPort.save(user);
    }

}
