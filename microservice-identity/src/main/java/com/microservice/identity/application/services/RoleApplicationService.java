package com.microservice.identity.application.services;

import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.in.role.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application Service that implements all role-related use case interfaces.
 * This service acts as a facade that delegates to the actual use case
 * implementations,
 * providing a unified interface for the infrastructure layer (controllers).
 * 
 * Follows the Facade pattern and Dependency Inversion Principle.
 */
@Service
public class RoleApplicationService implements
        CreateRoleUseCase,
        UpdateRoleUseCase,
        DeleteRoleUseCase,
        GetRoleByIdUseCase,
        GetRoleByNameUseCase,
        GetAllRolesUseCase,
        AssignRoleToUserUseCase,
        RemoveRoleFromUserUseCase {

    private final CreateRoleUseCase createRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final GetRoleByIdUseCase getRoleByIdUseCase;
    private final GetRoleByNameUseCase getRoleByNameUseCase;
    private final GetAllRolesUseCase getAllRolesUseCase;
    private final AssignRoleToUserUseCase assignRoleToUserUseCase;
    private final RemoveRoleFromUserUseCase removeRoleFromUserUseCase;

    public RoleApplicationService(
            CreateRoleUseCase createRoleUseCase,
            UpdateRoleUseCase updateRoleUseCase,
            DeleteRoleUseCase deleteRoleUseCase,
            GetRoleByIdUseCase getRoleByIdUseCase,
            GetRoleByNameUseCase getRoleByNameUseCase,
            GetAllRolesUseCase getAllRolesUseCase,
            AssignRoleToUserUseCase assignRoleToUserUseCase,
            RemoveRoleFromUserUseCase removeRoleFromUserUseCase) {
        this.createRoleUseCase = createRoleUseCase;
        this.updateRoleUseCase = updateRoleUseCase;
        this.deleteRoleUseCase = deleteRoleUseCase;
        this.getRoleByIdUseCase = getRoleByIdUseCase;
        this.getRoleByNameUseCase = getRoleByNameUseCase;
        this.getAllRolesUseCase = getAllRolesUseCase;
        this.assignRoleToUserUseCase = assignRoleToUserUseCase;
        this.removeRoleFromUserUseCase = removeRoleFromUserUseCase;
    }

    @Override
    public Role createRole(String name, String description) {
        return createRoleUseCase.createRole(name, description);
    }

    @Override
    public void updateRole(UUID id, String name, String description) {
        updateRoleUseCase.updateRole(id, name, description);
    }

    @Override
    public void deleteRole(UUID id) {
        deleteRoleUseCase.deleteRole(id);
    }

    @Override
    public Optional<Role> getRoleById(UUID id) {
        return getRoleByIdUseCase.getRoleById(id);
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        return getRoleByNameUseCase.getRoleByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return getAllRolesUseCase.getAllRoles();
    }

    @Override
    public void assignRoleToUser(UUID userId, UUID roleId) {
        assignRoleToUserUseCase.assignRoleToUser(userId, roleId);
    }

    @Override
    public void removeRoleFromUser(UUID userId, UUID roleId) {
        removeRoleFromUserUseCase.removeRoleFromUser(userId, roleId);
    }
}
