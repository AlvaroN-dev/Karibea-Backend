package com.microservice.identity.application.usecases.user;

import com.microservice.identity.application.exception.ValidationException;
import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.UpdateUserProfileUseCase;
import com.microservice.identity.domain.port.out.EventPublisherPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;
import com.microservice.identity.domain.service.UserService;

import java.util.UUID;

public class UpdateUserProfileUseCaseImpl implements UpdateUserProfileUseCase {

    private final UserService userService;
    private final UserRepositoryPort userRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    public UpdateUserProfileUseCaseImpl(UserService userService, UserRepositoryPort userRepositoryPort,
            EventPublisherPort eventPublisherPort) {
        this.userService = userService;
        this.userRepositoryPort = userRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public void updateUserProfile(UUID userId, String email, String name) {
        validateInput(email, name);

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userService.validateUserUpdate(user, email, name);

        user.updateProfile(email, name);

        userRepositoryPort.save(user);

        eventPublisherPort.publishUserCreated(user);

    }

    private void validateInput(String email, String name) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("email", "Email cannot be empty");
        }

        if (!isValidEmail(email)) {
            throw new ValidationException("email", "Invalid email format");
        }

        if (name != null && name.length() > 100) {
            throw new ValidationException("name", "Name cannot exceed 100 characters");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
