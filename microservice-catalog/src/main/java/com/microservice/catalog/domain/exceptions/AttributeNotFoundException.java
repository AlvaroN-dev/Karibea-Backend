package com.microservice.catalog.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when an attribute is not found.
 */
public class AttributeNotFoundException extends DomainException {

    private static final String ERROR_CODE = "ATTRIBUTE_NOT_FOUND";

    public AttributeNotFoundException(UUID attributeId) {
        super("Attribute not found with ID: " + attributeId, ERROR_CODE);
    }
}
