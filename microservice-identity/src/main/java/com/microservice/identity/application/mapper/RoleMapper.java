package com.microservice.identity.application.mapper;

import com.microservice.identity.application.dto.response.RoleResponse;
import com.microservice.identity.domain.models.Role;

import java.util.Collections;
import java.util.List;


/**
 * Mapper for transforming Role domain models to RoleResponse DTOs.
 * Follows stateless utility class pattern with static methods.
 * All methods are null-safe and handle edge cases gracefully.
 */
public class RoleMapper {

    // Private constructor to prevent instantiation
    private RoleMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converts a Role domain model to a RoleResponse DTO.
     *
     * @param role the Role domain model
     * @return RoleResponse DTO, or null if input is null
     */
    public static RoleResponse toResponse(Role role) {
        if (role == null) {
            return null;
        }

        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());

        // Map access level if present
        if (role.getAccessLevel() != null) {
            response.setAccessLevel(AccessLevelMapper.toResponse(role.getAccessLevel()));
        }

        return response;
    }

    /**
     * Converts a list of Role domain models to a list of RoleResponse DTOs.
     *
     * @param roles the list of Role domain models
     * @return list of RoleResponse DTOs, or empty list if input is null or empty
     */
    public static List<RoleResponse> toResponseList(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(RoleMapper::toResponse)
                .toList();
    }
}
