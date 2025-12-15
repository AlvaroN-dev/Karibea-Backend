package com.microservice.identity.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response DTO for role information.
 * Contains role data to be returned to clients.
 */
@Schema(description = "Role information response")
public class RoleResponse {

    @Schema(description = "Unique identifier of the role", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID id;

    @Schema(description = "Name of the role", example = "ADMIN")
    private String name;

    @Schema(description = "Description of the role", example = "Administrator with full system access")
    private String description;

    @Schema(description = "Access level associated with the role", example = "ADMIN")
    private String accessLevel;

    // Constructors
    public RoleResponse() {
    }

    public RoleResponse(UUID id, String name, String description, String accessLevel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.accessLevel = accessLevel;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
}
