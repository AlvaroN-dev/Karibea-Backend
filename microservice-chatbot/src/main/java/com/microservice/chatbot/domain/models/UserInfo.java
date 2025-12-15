package com.microservice.chatbot.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Domain model for external user information.
 * Represents user data retrieved from Identity microservice.
 * Location: domain/models - Pure domain model, no infrastructure dependencies.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private UUID id;
    private String username;
    private String email;

    /**
     * Creates an empty UserInfo for cases where user data is not available.
     */
    public static UserInfo empty() {
        return UserInfo.builder()
                .id(null)
                .username("Unknown")
                .email(null)
                .build();
    }

    /**
     * Creates a UserInfo with only the ID (fallback when external service is
     * unavailable).
     */
    public static UserInfo fromId(UUID userId) {
        return UserInfo.builder()
                .id(userId)
                .username(null)
                .email(null)
                .build();
    }
}
