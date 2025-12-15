package com.microservice.identity.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating user profile information.
 * All fields are optional - only provided fields will be updated.
 */
@Schema(description = "Request to update user profile information")
public class UpdateUserRequest {

    @Schema(description = "New email address for the user", example = "newemail@example.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Full name of the user", example = "John Doe", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 100)
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    // Constructors
    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
