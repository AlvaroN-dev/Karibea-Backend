package com.microservice.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for updating user preferences.
 * Contains settings for language, currency, and notifications.
 */
@Schema(description = "Request to update user preferences")
public class UpdatePreferencesRequest {

    @Schema(
        description = "External user ID from the identity service",
        example = "550e8400-e29b-41d4-a716-446655440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "External user ID is required")
    private UUID externalUserId;

    @Schema(
        description = "Preferred language ID from the catalog",
        example = "550e8400-e29b-41d4-a716-446655440002"
    )
    private UUID languageId;

    @Schema(
        description = "Preferred currency ID from the catalog",
        example = "550e8400-e29b-41d4-a716-446655440003"
    )
    private UUID currencyId;

    @Schema(
        description = "Whether to receive email notifications",
        example = "true"
    )
    private Boolean notificationEmail;

    @Schema(
        description = "Whether to receive push notifications",
        example = "true"
    )
    private Boolean notificationPush;

    // Constructors
    public UpdatePreferencesRequest() {
    }

    public UpdatePreferencesRequest(UUID externalUserId, UUID languageId, UUID currencyId,
                                    Boolean notificationEmail, Boolean notificationPush) {
        this.externalUserId = externalUserId;
        this.languageId = languageId;
        this.currencyId = currencyId;
        this.notificationEmail = notificationEmail;
        this.notificationPush = notificationPush;
    }

    // Getters and Setters
    public UUID getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(UUID externalUserId) {
        this.externalUserId = externalUserId;
    }

    public UUID getLanguageId() {
        return languageId;
    }

    public void setLanguageId(UUID languageId) {
        this.languageId = languageId;
    }

    public UUID getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(UUID currencyId) {
        this.currencyId = currencyId;
    }

    public Boolean getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(Boolean notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public Boolean getNotificationPush() {
        return notificationPush;
    }

    public void setNotificationPush(Boolean notificationPush) {
        this.notificationPush = notificationPush;
    }
}
