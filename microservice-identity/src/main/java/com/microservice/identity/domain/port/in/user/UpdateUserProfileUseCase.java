package com.microservice.identity.domain.port.in.user;

import java.util.UUID;

public interface UpdateUserProfileUseCase {
    void updateUserProfile(UUID userId, String email, String name);
}
