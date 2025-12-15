package com.microservice.identity.application.mapper;

import com.microservice.identity.domain.models.AccessLevel;

/**
 * Mapper for transforming AccessLevel domain models to String representations.
 * Follows stateless utility class pattern with static methods.
 * All methods are null-safe and handle edge cases gracefully.
 */
public class AccessLevelMapper {

    // Private constructor to prevent instantiation
    private AccessLevelMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converts an AccessLevel domain model to its String name representation.
     *
     * @param accessLevel the AccessLevel domain model
     * @return the name of the access level, or null if input is null
     */
    public static String toResponse(AccessLevel accessLevel) {
        if (accessLevel == null) {
            return null;
        }

        return accessLevel.getName();
    }
}
