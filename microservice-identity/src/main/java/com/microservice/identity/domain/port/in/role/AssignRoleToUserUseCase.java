package com.microservice.identity.domain.port.in.role;

import java.util.UUID;

public interface AssignRoleToUserUseCase {
    void assignRoleToUser(UUID userId, UUID roleId);
}
