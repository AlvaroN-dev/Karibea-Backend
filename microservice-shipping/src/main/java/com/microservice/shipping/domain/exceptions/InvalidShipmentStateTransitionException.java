package com.microservice.shipping.domain.exceptions;

/**
 * Exception thrown when a shipment state transition is not allowed.
 */
public class InvalidShipmentStateTransitionException extends ShipmentDomainException {

    public InvalidShipmentStateTransitionException(String message) {
        super("INVALID_STATE_TRANSITION", message);
    }
}
