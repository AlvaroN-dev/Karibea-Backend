package com.microservice.shopcart.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for creating a new shopping cart.
 */
@Schema(description = "Request to create a new shopping cart")
public class CreateCartRequest {

    @Schema(description = "External user profile ID (UUID) for authenticated users", 
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID userProfileId;

    @Schema(description = "Session ID for tracking guest users", 
            example = "sess_abc123xyz789",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minLength = 10,
            maxLength = 255)
    @Size(min = 10, max = 255, message = "Session ID must be between 10 and 255 characters")
    private String sessionId;

    @Schema(description = "Currency code (ISO 4217) for the cart", 
            example = "USD",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            defaultValue = "USD",
            minLength = 3,
            maxLength = 3)
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    // Constructors
    public CreateCartRequest() {
    }

    public CreateCartRequest(UUID userProfileId, String sessionId, String currency) {
        this.userProfileId = userProfileId;
        this.sessionId = sessionId;
        this.currency = currency;
    }

    // Getters and Setters
    public UUID getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(UUID userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
