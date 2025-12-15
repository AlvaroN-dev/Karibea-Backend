package com.microservice.identity.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for role creation and updates.
 * Contains role information including name and description.
 */
@Schema(description = "Request to create or update a role")
public class RoleRequest {

    @Schema(description = "Unique name of the role", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 3, maxLength = 50)
    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
    private String name;

    @Schema(description = "Description of the role and its permissions", example = "Administrator with full system access", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 255)
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    // Constructors
    public RoleRequest() {
    }

    public RoleRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
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
}
