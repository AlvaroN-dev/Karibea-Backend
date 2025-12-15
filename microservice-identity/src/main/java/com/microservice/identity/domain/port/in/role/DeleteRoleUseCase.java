package com.microservice.identity.domain.port.in.role;

import java.util.UUID;

public interface DeleteRoleUseCase {
    void deleteRole(UUID id);
}
