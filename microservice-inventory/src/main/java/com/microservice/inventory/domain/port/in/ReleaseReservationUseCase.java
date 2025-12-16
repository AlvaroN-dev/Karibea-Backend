package com.microservice.inventory.domain.port.in;

import java.util.UUID;

/**
 * Port IN - Release a stock reservation.
 * SRP: Only handles reservation release.
 */
public interface ReleaseReservationUseCase {

    void execute(ReleaseReservationCommand command);

    record ReleaseReservationCommand(
            UUID reservationId,
            String reason) {
        public ReleaseReservationCommand {
            if (reservationId == null)
                throw new IllegalArgumentException("Reservation ID is required");
        }
    }
}
