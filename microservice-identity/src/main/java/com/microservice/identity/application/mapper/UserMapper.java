package com.microservice.identity.application.mapper;

import com.microservice.identity.application.dto.response.UserResponse;
import com.microservice.identity.domain.models.User;

import java.util.Collections;
import java.util.List;

/**
 * Mapper for transforming User domain models to UserResponse DTOs.
 * Follows stateless utility class pattern with static methods.
 * All methods are null-safe and handle edge cases gracefully.
 */
public class UserMapper {

    // Private constructor to prevent instantiation
    private UserMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converts a User domain model to a UserResponse DTO.
     *
     * @param user the User domain model
     * @return UserResponse DTO, or null if input is null
     */
    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setName(null); // User model doesn't have name field
        response.setEnabled(user.isEnabled());
        response.setEmailVerified(user.isEmailVerified());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastLogin(user.getLastLogin());

        // Map roles if present
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            response.setRoles(
                    user.getRoles().stream()
                            .map(RoleMapper::toResponse)
                            .toList());
        } else {
            response.setRoles(Collections.emptyList());
        }

        return response;
    }

    /**
     * Converts a list of User domain models to a list of UserResponse DTOs.
     *
     * @param users the list of User domain models
     * @return list of UserResponse DTOs, or empty list if input is null or empty
     */
    public static List<UserResponse> toResponseList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }
}
