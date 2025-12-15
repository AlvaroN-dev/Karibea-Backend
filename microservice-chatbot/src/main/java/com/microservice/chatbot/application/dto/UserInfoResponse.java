package com.microservice.chatbot.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for user information enrichment.
 * Contains user data retrieved from Identity microservice.
 * Location: application/dto - Response DTO for user info.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information from Identity microservice")
public class UserInfoResponse {

    @Schema(description = "User unique identifier", example = "550e8400-e29b-41d4-a716-446655440010")
    private UUID id;

    @Schema(description = "Username of the user", example = "johndoe")
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    /**
     * Creates an empty UserInfoResponse for unknown users.
     */
    public static UserInfoResponse empty() {
        return UserInfoResponse.builder()
                .id(null)
                .username("Unknown")
                .email(null)
                .build();
    }

    /**
     * Creates a UserInfoResponse with only the ID (fallback).
     */
    public static UserInfoResponse fromId(UUID userId) {
        return UserInfoResponse.builder()
                .id(userId)
                .username(null)
                .email(null)
                .build();
    }
}
