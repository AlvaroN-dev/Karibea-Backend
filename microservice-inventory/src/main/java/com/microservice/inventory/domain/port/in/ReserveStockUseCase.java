package com.microservice.inventory.domain.port.in;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservice.inventory.domain.models.StockReservation;
import com.microservice.inventory.domain.models.enums.ReservationType;

/**
 * Port IN - Reserve stock for cart/checkout.
 * SRP: Only handles stock reservation.
 */
public interface ReserveStockUseCase {

    StockReservation execute(ReserveStockCommand command);

    record ReserveStockCommand(
            UUID stockId,
            int quantity,
            ReservationType reservationType,
            UUID externalCartId,
            UUID externalOrderId,
            LocalDateTime expiresAt) {
        public ReserveStockCommand {
            if (stockId == null)
                throw new IllegalArgumentException("Stock ID is required");
            if (quantity <= 0)
                throw new IllegalArgumentException("Quantity must be positive");
            if (reservationType == null)
                throw new IllegalArgumentException("Reservation type is required");
        }
    }
}
