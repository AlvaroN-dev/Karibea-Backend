package com.microservice.user.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para actualizar preferencias del usuario
 */
public record UpdatePreferencesRequest(
    
    @NotNull(message = "External user ID is required")
    UUID externalUserId,
    
    UUID languageId,
    
    UUID currencyId,
    
    Boolean notificationEmail,
    
    Boolean notificationPush
) {}
