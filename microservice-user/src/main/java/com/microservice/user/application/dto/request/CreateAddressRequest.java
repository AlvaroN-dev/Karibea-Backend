package com.microservice.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO para crear una dirección
 */
public record CreateAddressRequest(
    
    @NotNull(message = "External user ID is required")
    UUID externalUserId,
    
    @Size(max = 50, message = "Label must be less than 50 characters")
    String label,
    
    @NotBlank(message = "Street address is required")
    String streetAddress,
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be less than 100 characters")
    String city,
    
    @Size(max = 100, message = "State must be less than 100 characters")
    String state,
    
    @Size(max = 50, message = "Postal code must be less than 50 characters")
    String postalCode,
    
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be less than 100 characters")
    String country,
    
    boolean isDefault
) {}
