package com.microservice.identity.application.usecases.role;

import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.in.role.GetRoleByIdUseCase;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;

import java.util.Optional;
import java.util.UUID;

public class GetRoleByIdUseCaseImpl implements GetRoleByIdUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public GetRoleByIdUseCaseImpl(RoleRepositoryPort roleRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public Optional<Role> getRoleById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }
        return roleRepositoryPort.findById(id);
    }
}
