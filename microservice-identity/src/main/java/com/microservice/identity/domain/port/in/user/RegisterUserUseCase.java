package com.microservice.identity.domain.port.in.user;

import com.microservice.identity.domain.models.User;

public interface RegisterUserUseCase {
    User registerUser(String username, String email, String password);
}
