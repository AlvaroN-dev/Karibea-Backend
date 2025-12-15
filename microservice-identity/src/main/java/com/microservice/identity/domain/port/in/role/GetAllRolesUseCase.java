package com.microservice.identity.domain.port.in.role;

import com.microservice.identity.domain.models.Role;

import java.util.List;

public interface GetAllRolesUseCase {
    List<Role> getAllRoles();
}
