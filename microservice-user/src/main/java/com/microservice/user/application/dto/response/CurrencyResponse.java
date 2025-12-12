package com.microservice.user.application.dto.response;

import java.util.UUID;

/**
 * DTO de respuesta para moneda
 */
public record CurrencyResponse(
    UUID id,
    String name,
    String code
) {}
