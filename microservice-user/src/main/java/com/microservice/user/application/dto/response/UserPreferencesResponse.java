package com.microservice.user.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para preferencias del usuario
 */
public record UserPreferencesResponse(
    UUID id,
    UUID externalUserId,
    LanguageResponse language,
    CurrencyResponse currency,
    boolean notificationEmail,
    boolean notificationPush,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
