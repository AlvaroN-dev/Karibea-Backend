package com.microservice.user.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para dirección
 */
public record AddressResponse(
    UUID id,
    UUID externalUserId,
    String label,
    String streetAddress,
    String city,
    String state,
    String postalCode,
    String country,
    String formattedAddress,
    boolean isDefault,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
