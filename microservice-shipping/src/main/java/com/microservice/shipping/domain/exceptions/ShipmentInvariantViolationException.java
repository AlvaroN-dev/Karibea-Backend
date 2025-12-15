package com.microservice.shipping.domain.exceptions;

/**
 * Exception thrown when a domain invariant is violated.
 */
public class ShipmentInvariantViolationException extends ShipmentDomainException {

    public ShipmentInvariantViolationException(String message) {
        super("INVARIANT_VIOLATION", message);
    }
}
