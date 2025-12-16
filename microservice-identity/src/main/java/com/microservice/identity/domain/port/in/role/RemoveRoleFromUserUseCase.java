package com.microservice.identity.domain.port.in.role;

import java.util.UUID;

public interface RemoveRoleFromUserUseCase {
    void removeRoleFromUser(UUID userId, UUID roleId);
}
