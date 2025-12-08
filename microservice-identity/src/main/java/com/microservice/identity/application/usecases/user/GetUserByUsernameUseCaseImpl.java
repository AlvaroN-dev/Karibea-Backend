package com.microservice.identity.application.usecases.user;

import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.GetUserByUsernameUseCase;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

public class GetUserByUsernameUseCaseImpl implements GetUserByUsernameUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserByUsernameUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }


    @Override
    public User getUserByUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username ));
    }
}
