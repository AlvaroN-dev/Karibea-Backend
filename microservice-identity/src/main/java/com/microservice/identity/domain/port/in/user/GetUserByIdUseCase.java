package com.microservice.identity.domain.port.in.user;

import com.microservice.identity.domain.models.User;

import java.util.UUID;

public interface GetUserByIdUseCase {

    User getUserById(UUID id);

}
