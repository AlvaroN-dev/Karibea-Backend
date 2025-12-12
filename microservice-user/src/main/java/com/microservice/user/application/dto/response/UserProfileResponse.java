package com.microservice.user.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para perfil de usuario
 */
public record UserProfileResponse(
    UUID id,
    UUID externalUserId,
    String firstName,
    String lastName,
    String middleName,
    String secondLastname,
    String fullName,
    String phone,
    GenderResponse gender,
    String avatarUrl,
    LocalDate dateOfBirth,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
