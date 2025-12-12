package com.microservice.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para crear un perfil de usuario
 */
public record CreateUserProfileRequest(
    
    @NotNull(message = "External user ID is required")
    UUID externalUserId,
    
    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must be less than 255 characters")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must be less than 255 characters")
    String lastName,
    
    @Size(max = 255, message = "Middle name must be less than 255 characters")
    String middleName,
    
    @Size(max = 255, message = "Second lastname must be less than 255 characters")
    String secondLastname,
    
    @Size(max = 100, message = "Phone must be less than 100 characters")
    String phone,
    
    UUID genderId,
    
    String avatarUrl,
    
    LocalDate dateOfBirth
) {}
