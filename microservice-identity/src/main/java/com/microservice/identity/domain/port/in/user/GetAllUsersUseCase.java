package com.microservice.identity.domain.port.in.user;

import com.microservice.identity.domain.models.User;

import java.util.List;

public interface GetAllUsersUseCase {
    List<User> getUsers();

}
