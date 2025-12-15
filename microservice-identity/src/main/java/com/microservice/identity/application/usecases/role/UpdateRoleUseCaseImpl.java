package com.microservice.identity.application.usecases.role;

import com.microservice.identity.application.exception.ValidationException;
import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.in.role.UpdateRoleUseCase;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;

import java.util.Optional;
import java.util.UUID;

public class UpdateRoleUseCaseImpl implements UpdateRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public UpdateRoleUseCaseImpl(RoleRepositoryPort roleRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public void updateRole(UUID id, String name, String description) {
        validateInput(id, name, description);

        // Find existing role
        Role role = roleRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));

        // Check if name is being changed and if new name conflicts with existing role
        if (!role.getName().equals(name)) {
            Optional<Role> existingRole = roleRepositoryPort.findByName(name);
            if (existingRole.isPresent() && !existingRole.get().getId().equals(id)) {
                throw new ValidationException("name", "Role with name '" + name + "' already exists");
            }
        }

        // Update role using protected setters (domain model pattern)
        updateRoleProperties(role, name, description);

        // Save updated role
        roleRepositoryPort.save(role);
    }

    private void updateRoleProperties(Role role, String name, String description) {
        // Use domain logic to update role
        role.update(name, description);
    }

    private void validateInput(UUID id, String name, String description) {
        if (id == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }

        if (name == null || name.isBlank()) {
            throw new ValidationException("name", "Role name cannot be null or empty");
        }

        if (name.length() > 50) {
            throw new ValidationException("name", "Role name cannot exceed 50 characters");
        }

        if (description != null && description.length() > 255) {
            throw new ValidationException("description", "Role description cannot exceed 255 characters");
        }
    }
}
