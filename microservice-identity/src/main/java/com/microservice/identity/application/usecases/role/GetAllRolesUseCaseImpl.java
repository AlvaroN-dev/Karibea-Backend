package com.microservice.identity.application.usecases.role;

import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.port.in.role.GetAllRolesUseCase;
import com.microservice.identity.domain.port.out.RoleRepositoryPort;

import java.util.List;

public class GetAllRolesUseCaseImpl implements GetAllRolesUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public GetAllRolesUseCaseImpl(RoleRepositoryPort roleRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepositoryPort.findAll();
    }
}
