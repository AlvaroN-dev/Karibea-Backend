package com.microservice.user.application.dto.response;

import java.util.UUID;

/**
 * DTO de respuesta para idioma
 */
public record LanguageResponse(
    UUID id,
    String name,
    String code
) {}
