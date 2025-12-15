package com.microservice.identity.application.usecases.role;

import com.microservice.identity.domain.exceptions.UserNotFoundException;
import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.in.role.RemoveRoleFromUserUseCase;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;
import com.microservice.identity.domain.port.out.UserRepositoryPort;

import java.util.UUID;

public class RemoveRoleFromUserUseCaseImpl implements RemoveRoleFromUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;

    public RemoveRoleFromUserUseCaseImpl(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public void removeRoleFromUser(UUID userId, UUID roleId) {
        validateInput(userId, roleId);

        // Find user
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Find role
        Role role = roleRepositoryPort.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));

        // Remove role from user using domain method
        user.removeRole(role);

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
