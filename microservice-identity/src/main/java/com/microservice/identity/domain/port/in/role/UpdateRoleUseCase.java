package com.microservice.identity.domain.port.in.role;

import java.util.UUID;

public interface UpdateRoleUseCase {
    void updateRole(UUID id, String name, String description);
}
