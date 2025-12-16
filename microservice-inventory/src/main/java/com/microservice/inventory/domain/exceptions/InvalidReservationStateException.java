package com.microservice.inventory.domain.exceptions;

import com.microservice.inventory.domain.models.enums.ReservationStatus;

/**
 * Thrown when an invalid reservation state transition is attempted.
 */
public class InvalidReservationStateException extends DomainException {

    public InvalidReservationStateException(ReservationStatus current, ReservationStatus target) {
        super(
                String.format("Cannot transition reservation from %s to %s", current, target),
                "INVALID_RESERVATION_STATE");
    }
}
