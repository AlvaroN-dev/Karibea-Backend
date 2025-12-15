package com.microservice.inventory.domain.port.in;

import java.util.UUID;

/**
 * Port IN - Confirm a reservation (after order confirmation).
 * SRP: Only handles reservation confirmation.
 */
public interface ConfirmReservationUseCase {

    void execute(ConfirmReservationCommand command);

    record ConfirmReservationCommand(
            UUID reservationId,
            UUID externalOrderId,
            UUID performedById) {
        public ConfirmReservationCommand {
            if (reservationId == null)
                throw new IllegalArgumentException("Reservation ID is required");
        }
    }
}
