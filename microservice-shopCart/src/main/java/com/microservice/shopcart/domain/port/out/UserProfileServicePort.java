package com.microservice.shopcart.domain.port.out;

import java.util.UUID;

/**
 * Output port for fetching user profile information from User microservice.
 */
public interface UserProfileServicePort {
    
    /**
     * Gets user profile information by user ID.
     *
     * @param userId The user ID
     * @return User profile information
     */
    UserProfileInfo getUserProfile(UUID userId);
    
    /**
     * User profile information DTO from external service.
     */
    record UserProfileInfo(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phone
    ) {}
}
