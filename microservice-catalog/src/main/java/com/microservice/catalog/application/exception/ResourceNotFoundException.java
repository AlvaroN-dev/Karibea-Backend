package com.microservice.catalog.application.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends ApplicationException {

    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String resourceType, Object identifier) {
        super(resourceType + " not found with identifier: " + identifier, ERROR_CODE);
    }
}
