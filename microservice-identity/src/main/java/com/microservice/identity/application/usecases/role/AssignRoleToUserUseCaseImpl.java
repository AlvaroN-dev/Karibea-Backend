package com.microservice.identity.application.usecases.role;

import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.role.AssignRoleToUserUseCase;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class AssignRoleToUserUseCaseImpl implements AssignRoleToUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;

    public AssignRoleToUserUseCaseImpl(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public void assignRoleToUser(UUID userId, UUID roleId) {
        validateInput(userId, roleId);

        // Find user
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Verify user is not deleted
        if (user.isDeleted()) {
            throw new IllegalStateException("Cannot assign role to deleted user");
        }

        // Find role
        Role role = roleRepositoryPort.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));

        // Check if user already has this role (idempotent operation)
        if (user.hasRole(role.getName())) {
            // User already has this role, operation is idempotent - do nothing
            return;
        }

        // Assign role to user using domain method
        user.addRole(role);

        // Persist changes
        userRepositoryPort.save(user);
    }

    private void validateInput(UUID userId, UUID roleId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (roleId == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }
    }
}
