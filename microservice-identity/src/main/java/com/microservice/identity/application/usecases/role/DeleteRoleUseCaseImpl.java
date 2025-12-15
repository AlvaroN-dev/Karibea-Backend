package com.microservice.identity.application.usecases.role;

import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.in.role.DeleteRoleUseCase;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;

import java.util.UUID;

public class DeleteRoleUseCaseImpl implements DeleteRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public DeleteRoleUseCaseImpl(RoleRepositoryPort roleRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public void deleteRole(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }

        Role role = roleRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));

        roleRepositoryPort.delete(role);
    }
}
