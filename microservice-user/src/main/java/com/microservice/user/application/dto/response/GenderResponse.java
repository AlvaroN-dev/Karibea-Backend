package com.microservice.user.application.dto.response;

import java.util.UUID;

/**
 * DTO de respuesta para género
 */
public record GenderResponse(
    UUID id,
    String name
) {}
