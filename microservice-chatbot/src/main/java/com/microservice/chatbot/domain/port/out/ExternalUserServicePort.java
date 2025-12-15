package com.microservice.chatbot.domain.port.out;

import com.microservice.chatbot.domain.models.UserInfo;

import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT for retrieving external user information.
 * Used to enrich conversation responses with user details from Identity
 * microservice.
 * Location: domain/port/out - Outbound port for external service integration.
 */
public interface ExternalUserServicePort {

    /**
     * Retrieves user information by user ID.
     *
     * @param userId the external user profile ID
     * @return Optional containing UserInfo if found, empty otherwise
     */
    Optional<UserInfo> getUserById(UUID userId);

    /**
     * Retrieves user information with fallback to ID-only UserInfo if service is
     * unavailable.
     *
     * @param userId the external user profile ID
     * @return UserInfo with available data, or ID-only fallback
     */
    default UserInfo getUserOrFallback(UUID userId) {
        if (userId == null) {
            return UserInfo.empty();
        }
        return getUserById(userId).orElse(UserInfo.fromId(userId));
    }
}
