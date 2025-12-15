package com.microservice.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for user preferences information.
 */
@Schema(description = "User preferences information response")
public class UserPreferencesResponse {

    @Schema(description = "Unique preferences ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "External user ID", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID externalUserId;

    @Schema(description = "User's preferred language")
    private LanguageResponse language;

    @Schema(description = "User's preferred currency")
    private CurrencyResponse currency;

    @Schema(description = "Email notifications enabled", example = "true")
    private boolean notificationEmail;

    @Schema(description = "Push notifications enabled", example = "true")
    private boolean notificationPush;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-06-20T14:45:00")
    private LocalDateTime updatedAt;

    // Constructors
    public UserPreferencesResponse() {
    }

    public UserPreferencesResponse(UUID id, UUID externalUserId, LanguageResponse language,
                                   CurrencyResponse currency, boolean notificationEmail,
                                   boolean notificationPush, LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {
        this.id = id;
        this.externalUserId = externalUserId;
        this.language = language;
        this.currency = currency;
        this.notificationEmail = notificationEmail;
        this.notificationPush = notificationPush;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getExternalUserId() { return externalUserId; }
    public void setExternalUserId(UUID externalUserId) { this.externalUserId = externalUserId; }
    public LanguageResponse getLanguage() { return language; }
    public void setLanguage(LanguageResponse language) { this.language = language; }
    public CurrencyResponse getCurrency() { return currency; }
    public void setCurrency(CurrencyResponse currency) { this.currency = currency; }
    public boolean isNotificationEmail() { return notificationEmail; }
    public void setNotificationEmail(boolean notificationEmail) { this.notificationEmail = notificationEmail; }
    public boolean isNotificationPush() { return notificationPush; }
    public void setNotificationPush(boolean notificationPush) { this.notificationPush = notificationPush; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
