package com.microservice.order.domain.exceptions;

/**
 * Exception thrown when a domain invariant is violated.
 */
public class OrderInvariantViolationException extends OrderDomainException {

    public OrderInvariantViolationException(String message) {
        super("INVARIANT_VIOLATION", message);
    }
}
