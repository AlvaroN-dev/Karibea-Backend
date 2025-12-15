package com.microservice.identity.application.usecases.role;

import com.microservice.identity.application.exception.ValidationException;
import com.microservice.identity.domain.models.AccessLevel;
import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.in.role.CreateRoleUseCase;
import com.microservice.identity.domain.port.out.AccessLevelRepositoryPort;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;

public class CreateRoleUseCaseImpl implements CreateRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;
    private final AccessLevelRepositoryPort accessLevelRepositoryPort;

    public CreateRoleUseCaseImpl(RoleRepositoryPort roleRepositoryPort,
            AccessLevelRepositoryPort accessLevelRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
        this.accessLevelRepositoryPort = accessLevelRepositoryPort;
    }

    @Override
    public Role createRole(String name, String description) {
        validateInput(name, description);

        // Check if role with same name already exists
        if (roleRepositoryPort.existsByName(name)) {
            throw new ValidationException("name", "Role with name '" + name + "' already exists");
        }

        // Fetch default AccessLevel (USER level)
        AccessLevel defaultAccessLevel = accessLevelRepositoryPort.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default AccessLevel 'USER' not found in system"));

        // Create and save role
        Role role = new Role(name, defaultAccessLevel, description);
        return roleRepositoryPort.save(role);
    }

    private void validateInput(String name, String description) {
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
