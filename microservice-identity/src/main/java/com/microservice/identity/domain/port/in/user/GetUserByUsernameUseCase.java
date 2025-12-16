package com.microservice.identity.domain.port.in.user;

import com.microservice.identity.domain.models.User;

public interface GetUserByUsernameUseCase {
    User getUserByUsername(String username);
}
