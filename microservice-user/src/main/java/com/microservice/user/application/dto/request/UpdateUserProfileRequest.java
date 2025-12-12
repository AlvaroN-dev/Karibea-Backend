package com.microservice.user.application.dto.request;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para actualizar un perfil de usuario
 */
public record UpdateUserProfileRequest(
    
    @Size(max = 255, message = "First name must be less than 255 characters")
    String firstName,
    
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
