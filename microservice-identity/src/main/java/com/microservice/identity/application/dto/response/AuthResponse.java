package com.microservice.identity.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for authentication.
 * Contains access token, refresh token, and user information after successful
 * authentication.
 */
@Schema(description = "Authentication response with tokens and user information")
public class AuthResponse {

    @Schema(description = "JWT access token for API authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "JWT refresh token for obtaining new access tokens", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Type of token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration time in seconds", example = "3600")
    private Long expiresIn;

    @Schema(description = "Authenticated user information")
    private UserResponse user;

    // Constructors
    public AuthResponse() {
    }

    public AuthResponse(String accessToken, String refreshToken, String tokenType, Long expiresIn, UserResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
