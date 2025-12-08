package com.microservice.identity.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for token operations.
 * Contains JWT token information for token refresh and validation responses.
 */
@Schema(description = "Token response for refresh and validation operations")
public class TokenResponse {

    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Type of token", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Token expiration time in seconds", example = "3600")
    private Long expiresIn;

    // Constructors
    public TokenResponse() {
    }

    public TokenResponse(String token, String type, Long expiresIn) {
        this.token = token;
        this.type = type;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
