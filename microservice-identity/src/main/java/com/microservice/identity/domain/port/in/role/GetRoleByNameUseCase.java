package com.microservice.identity.domain.port.in.role;

import com.microservice.identity.domain.models.Role;

import java.util.Optional;

public interface GetRoleByNameUseCase {
    Optional<Role> getRoleByName(String name);
}
