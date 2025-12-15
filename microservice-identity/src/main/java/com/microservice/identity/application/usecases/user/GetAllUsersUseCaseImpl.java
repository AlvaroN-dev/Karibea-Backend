package com.microservice.identity.application.usecases.user;

import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.user.GetAllUsersUseCase;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.List;

public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {

    private UserRepositoryPort userRepositoryPort;

    public GetAllUsersUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }


    @Override
    public List<User> getUsers() {
        return userRepositoryPort.findAll();
    }
}
