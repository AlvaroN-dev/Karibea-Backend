package com.microservice.identity.domain.port.in.user;

import java.util.UUID;

public interface DeleteUserUseCase {
    void deleteUser(UUID userId);
}
