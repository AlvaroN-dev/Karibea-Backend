package com.microservice.identity.application.usecases.role;

import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.in.role.GetRoleByNameUseCase;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;

import java.util.Optional;

public class GetRoleByNameUseCaseImpl implements GetRoleByNameUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public GetRoleByNameUseCaseImpl(RoleRepositoryPort roleRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        return roleRepositoryPort.findByName(name);
    }
}
