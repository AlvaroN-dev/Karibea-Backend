package com.microservice.identity.domain.port.in.role;

import com.microservice.identity.domain.models.Role;

import java.util.Optional;
import java.util.UUID;

public interface GetRoleByIdUseCase {
    Optional<Role> getRoleById(UUID id);
}
