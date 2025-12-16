package com.microservice.identity.domain.port.in.user;

import java.util.UUID;

public interface ChangePasswordUseCase {
    void changePassword(UUID userId, String currentPassword, String newPassword);
}
