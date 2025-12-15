package com.microservice.identity.application.usecases.user;

import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.DeleteUserUseCase;
import com.microservice.identity.domain.port.out.EventPublisherPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    public DeleteUserUseCaseImpl(UserRepositoryPort userRepositoryPort, EventPublisherPort eventPublisherPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.softDelete();

        userRepositoryPort.save(user);

        eventPublisherPort.publishUserDeleted(userId);
    }
}
