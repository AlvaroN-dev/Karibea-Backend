package com.microservice.identity.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for user information.
 * Contains user data to be returned to clients.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User information response")
public class UserResponse {

    @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Username of the user", example = "johndoe")
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Full name of the user", example = "John Doe")
    private String name;

    @Schema(description = "Whether the user account is enabled", example = "true")
    private boolean enabled;

    @Schema(description = "Whether the user's email has been verified", example = "true")
    private boolean emailVerified;

    @Schema(description = "List of roles assigned to the user")
    private List<RoleResponse> roles;

    @Schema(description = "Timestamp when the user was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp of the user's last login", example = "2024-01-20T14:25:00")
    private LocalDateTime lastLogin;

}
