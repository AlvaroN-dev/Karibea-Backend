package com.microservice.user.domain.exceptions;

import java.util.UUID;

/**
 * Excepción cuando no se encuentra una dirección
 */
public class AddressNotFoundException extends DomainException {
    
    private static final String CODE = "ADDRESS_NOT_FOUND";
    
    public AddressNotFoundException(UUID id) {
        super(CODE, String.format("Address with ID '%s' not found", id));
    }
}
