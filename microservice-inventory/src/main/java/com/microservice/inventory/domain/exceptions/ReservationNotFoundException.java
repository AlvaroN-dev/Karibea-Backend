package com.microservice.inventory.domain.exceptions;

import java.util.UUID;

/**
 * Thrown when reservation is not found.
 */
public class ReservationNotFoundException extends DomainException {

    public ReservationNotFoundException(UUID reservationId) {
        super(
                String.format("Reservation not found with ID: %s", reservationId),
                "RESERVATION_NOT_FOUND");
    }
}
