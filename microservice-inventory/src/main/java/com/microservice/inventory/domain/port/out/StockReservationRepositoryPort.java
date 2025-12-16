package com.microservice.inventory.domain.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.models.enums.ReservationStatus;

/**
 * Port OUT - Stock reservation repository contract.
 */
public interface StockReservationRepositoryPort {

    StockReservation save(StockReservation reservation);

    Optional<StockReservation> findById(UUID reservationId);

    List<StockReservation> findByStockId(UUID stockId);

    List<StockReservation> findByExternalCartId(UUID externalCartId);

    List<StockReservation> findByExternalOrderId(UUID externalOrderId);

    List<StockReservation> findExpiredReservations(LocalDateTime now);

    List<StockReservation> findByStatus(ReservationStatus status);
}
