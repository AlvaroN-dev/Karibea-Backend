package com.microservice.identity.application.usecases.user;

import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.GetUserByIdUseCase;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserByIdUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User getUserById(UUID id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}
