package com.microservice.user.application.dto.request;

import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar una dirección
 */
public record UpdateAddressRequest(
    
    @Size(max = 50, message = "Label must be less than 50 characters")
    String label,
    
    String streetAddress,
    
    @Size(max = 100, message = "City must be less than 100 characters")
    String city,
    
    @Size(max = 100, message = "State must be less than 100 characters")
    String state,
    
    @Size(max = 50, message = "Postal code must be less than 50 characters")
    String postalCode,
    
    @Size(max = 100, message = "Country must be less than 100 characters")
    String country
) {}
