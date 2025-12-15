package com.microservice.identity.domain.port.in.role;

import com.microservice.identity.domain.models.Role;

public interface CreateRoleUseCase {
    Role createRole(String name, String description);
}
