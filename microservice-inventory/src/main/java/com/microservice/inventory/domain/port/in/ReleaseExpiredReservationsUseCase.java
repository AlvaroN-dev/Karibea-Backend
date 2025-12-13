package com.microservice.inventory.domain.port.in;

/**
 * Port IN - Release expired reservations.
 * SRP: Only handles expired reservation cleanup.
 */
public interface ReleaseExpiredReservationsUseCase {

    int execute();
}
