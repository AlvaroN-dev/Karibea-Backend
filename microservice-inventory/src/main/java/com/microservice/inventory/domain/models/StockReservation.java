package com.microservice.inventory.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.microservice.inventory.domain.exceptions.InvalidReservationStateException;
import com.microservice.inventory.domain.models.enums.ReservationStatus;
import com.microservice.inventory.domain.models.enums.ReservationType;

/**
 * Entity representing a stock reservation.
 * Part of Stock aggregate context.
 */
public class StockReservation {

    private UUID id;
    private UUID stockId;
    private UUID externalOrderId;
    private UUID externalCartId;
    private int quantity;
    private ReservationType reservationType;
    private ReservationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private StockReservation() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getStockId() {
        return stockId;
    }

    public UUID getExternalOrderId() {
        return externalOrderId;
    }

    public UUID getExternalCartId() {
        return externalCartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Business methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isActive() {
        return !status.isTerminal() && !isExpired();
    }

    public void confirm() {
        if (!status.canTransitionTo(ReservationStatus.CONFIRMED)) {
            throw new InvalidReservationStateException(status, ReservationStatus.CONFIRMED);
        }
        this.status = ReservationStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (!status.canTransitionTo(ReservationStatus.COMPLETED)) {
            throw new InvalidReservationStateException(status, ReservationStatus.COMPLETED);
        }
        this.status = ReservationStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (!status.canTransitionTo(ReservationStatus.CANCELLED)) {
            throw new InvalidReservationStateException(status, ReservationStatus.CANCELLED);
        }
        this.status = ReservationStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void expire() {
        if (!status.canTransitionTo(ReservationStatus.EXPIRED)) {
            throw new InvalidReservationStateException(status, ReservationStatus.EXPIRED);
        }
        this.status = ReservationStatus.EXPIRED;
        this.updatedAt = LocalDateTime.now();
    }

    public static class Builder {
        private final StockReservation reservation = new StockReservation();

        public Builder id(UUID id) {
            reservation.id = id;
            return this;
        }

        public Builder stockId(UUID stockId) {
            reservation.stockId = stockId;
            return this;
        }

        public Builder externalOrderId(UUID id) {
            reservation.externalOrderId = id;
            return this;
        }

        public Builder externalCartId(UUID id) {
            reservation.externalCartId = id;
            return this;
        }

        public Builder quantity(int qty) {
            reservation.quantity = qty;
            return this;
        }

        public Builder reservationType(ReservationType type) {
            reservation.reservationType = type;
            return this;
        }

        public Builder status(ReservationStatus status) {
            reservation.status = status;
            return this;
        }

        public Builder expiresAt(LocalDateTime time) {
            reservation.expiresAt = time;
            return this;
        }

        public Builder createdAt(LocalDateTime time) {
            reservation.createdAt = time;
            return this;
        }

        public Builder updatedAt(LocalDateTime time) {
            reservation.updatedAt = time;
            return this;
        }

        public StockReservation build() {
            if (reservation.id == null)
                reservation.id = UUID.randomUUID();
            if (reservation.status == null)
                reservation.status = ReservationStatus.PENDING;
            if (reservation.createdAt == null)
                reservation.createdAt = LocalDateTime.now();
            if (reservation.updatedAt == null)
                reservation.updatedAt = reservation.createdAt;
            Objects.requireNonNull(reservation.stockId, "Stock ID is required");
            if (reservation.quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
            return reservation;
        }
    }
}
