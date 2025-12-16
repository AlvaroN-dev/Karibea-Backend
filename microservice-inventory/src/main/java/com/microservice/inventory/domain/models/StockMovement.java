package com.microservice.inventory.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.microservice.inventory.domain.models.enums.MovementType;

/**
 * Entity representing a stock movement (audit trail).
 * Part of Stock aggregate - NOT an Aggregate Root.
 */
public class StockMovement {

    private UUID id;
    private UUID stockId;
    private MovementType movementType;
    private int quantity;
    private String referenceType;
    private UUID externalReferenceId;
    private String note;
    private UUID externalPerformedById;
    private LocalDateTime createdAt;

    // Private constructor for builder
    private StockMovement() {
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

    public MovementType getMovementType() {
        return movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public UUID getExternalReferenceId() {
        return externalReferenceId;
    }

    public String getNote() {
        return note;
    }

    public UUID getExternalPerformedById() {
        return externalPerformedById;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isIncrease() {
        return movementType.increasesStock();
    }

    public boolean isDecrease() {
        return movementType.decreasesStock();
    }

    public static class Builder {
        private final StockMovement movement = new StockMovement();

        public Builder id(UUID id) {
            movement.id = id;
            return this;
        }

        public Builder stockId(UUID stockId) {
            movement.stockId = stockId;
            return this;
        }

        public Builder movementType(MovementType type) {
            movement.movementType = type;
            return this;
        }

        public Builder quantity(int quantity) {
            movement.quantity = quantity;
            return this;
        }

        public Builder referenceType(String type) {
            movement.referenceType = type;
            return this;
        }

        public Builder externalReferenceId(UUID id) {
            movement.externalReferenceId = id;
            return this;
        }

        public Builder note(String note) {
            movement.note = note;
            return this;
        }

        public Builder externalPerformedById(UUID id) {
            movement.externalPerformedById = id;
            return this;
        }

        public Builder createdAt(LocalDateTime time) {
            movement.createdAt = time;
            return this;
        }

        public StockMovement build() {
            if (movement.id == null)
                movement.id = UUID.randomUUID();
            if (movement.createdAt == null)
                movement.createdAt = LocalDateTime.now();
            Objects.requireNonNull(movement.stockId, "Stock ID is required");
            Objects.requireNonNull(movement.movementType, "Movement type is required");
            if (movement.quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
            return movement;
        }
    }
}
