package com.microservice.order.domain.exceptions;

/**
 * Exception thrown when an order state transition is not allowed.
 */
public class InvalidOrderStateTransitionException extends OrderDomainException {

    public InvalidOrderStateTransitionException(String message) {
        super("INVALID_STATE_TRANSITION", message);
    }
}
