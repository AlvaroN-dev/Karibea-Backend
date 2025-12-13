package com.microservice.inventory.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.microservice.inventory.domain.events.DomainEvent;
import com.microservice.inventory.domain.events.LowStockAlertEvent;
import com.microservice.inventory.domain.events.StockAdjustedEvent;
import com.microservice.inventory.domain.events.StockCreatedEvent;
import com.microservice.inventory.domain.events.StockReleasedEvent;
import com.microservice.inventory.domain.events.StockReservedEvent;
import com.microservice.inventory.domain.exceptions.InsufficientStockException;
import com.microservice.inventory.domain.models.enums.MovementType;
import com.microservice.inventory.domain.models.enums.ReservationStatus;
import com.microservice.inventory.domain.models.enums.ReservationType;

/**
 * Stock Aggregate Root.
 * Central entity managing inventory levels, movements and reservations.
 * 
 * <p>
 * <b>Invariants:</b>
 * </p>
 * <ul>
 * <li>quantityAvailable >= 0</li>
 * <li>quantityReserved >= 0</li>
 * <li>quantityReserved <= quantityAvailable + quantityReserved</li>
 * </ul>
 */
public class Stock {

    private UUID id;
    private UUID externalProductId;
    private UUID externalVariantId;
    private UUID warehouseId;
    private int quantityAvailable;
    private int quantityReserved;
    private int quantityIncoming;
    private int lowStockThreshold;
    private int reorderPoint;
    private LocalDateTime lastRestockedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<StockMovement> movements = new ArrayList<>();
    private List<StockReservation> reservations = new ArrayList<>();
    private List<DomainEvent> domainEvents = new ArrayList<>();

    private Stock() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // ========== Getters ==========
    public UUID getId() {
        return id;
    }

    public UUID getExternalProductId() {
        return externalProductId;
    }

    public UUID getExternalVariantId() {
        return externalVariantId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public int getQuantityIncoming() {
        return quantityIncoming;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public LocalDateTime getLastRestockedAt() {
        return lastRestockedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<StockMovement> getMovements() {
        return Collections.unmodifiableList(movements);
    }

    public List<StockReservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    // ========== Computed Properties ==========

    public int getTotalQuantity() {
        return quantityAvailable + quantityReserved;
    }

    public int getAvailableForReservation() {
        return quantityAvailable;
    }

    public boolean isLowStock() {
        return quantityAvailable <= lowStockThreshold;
    }

    public boolean needsReorder() {
        return quantityAvailable <= reorderPoint;
    }

    // ========== Business Methods ==========

    /**
     * Increase stock quantity (purchases, returns, adjustments).
     */
    public void increaseStock(int quantity, MovementType type, String referenceType,
            UUID externalReferenceId, UUID performedById, String note) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!type.increasesStock()) {
            throw new IllegalArgumentException("Movement type does not increase stock: " + type);
        }

        this.quantityAvailable += quantity;
        this.lastRestockedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        StockMovement movement = StockMovement.builder()
                .stockId(this.id)
                .movementType(type)
                .quantity(quantity)
                .referenceType(referenceType)
                .externalReferenceId(externalReferenceId)
                .externalPerformedById(performedById)
                .note(note)
                .build();

        this.movements.add(movement);

        registerEvent(new StockAdjustedEvent(
                this.id, this.externalVariantId, this.warehouseId,
                type, quantity, this.quantityAvailable, referenceType));
    }

    /**
     * Decrease stock quantity (sales, damages, adjustments).
     */
    public void decreaseStock(int quantity, MovementType type, String referenceType,
            UUID externalReferenceId, UUID performedById, String note) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!type.decreasesStock()) {
            throw new IllegalArgumentException("Movement type does not decrease stock: " + type);
        }
        if (quantity > this.quantityAvailable) {
            throw new InsufficientStockException(this.id, quantity, this.quantityAvailable);
        }

        this.quantityAvailable -= quantity;
        this.updatedAt = LocalDateTime.now();

        StockMovement movement = StockMovement.builder()
                .stockId(this.id)
                .movementType(type)
                .quantity(quantity)
                .referenceType(referenceType)
                .externalReferenceId(externalReferenceId)
                .externalPerformedById(performedById)
                .note(note)
                .build();

        this.movements.add(movement);

        registerEvent(new StockAdjustedEvent(
                this.id, this.externalVariantId, this.warehouseId,
                type, quantity, this.quantityAvailable, referenceType));

        checkLowStock();
    }

    /**
     * Reserve stock for cart/checkout.
     */
    public StockReservation reserveStock(int quantity, ReservationType type,
            UUID externalCartId, UUID externalOrderId,
            LocalDateTime expiresAt) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > this.quantityAvailable) {
            throw new InsufficientStockException(this.id, quantity, this.quantityAvailable);
        }

        this.quantityAvailable -= quantity;
        this.quantityReserved += quantity;
        this.updatedAt = LocalDateTime.now();

        StockReservation reservation = StockReservation.builder()
                .stockId(this.id)
                .quantity(quantity)
                .reservationType(type)
                .externalCartId(externalCartId)
                .externalOrderId(externalOrderId)
                .expiresAt(expiresAt)
                .status(ReservationStatus.PENDING)
                .build();

        this.reservations.add(reservation);

        registerEvent(new StockReservedEvent(
                this.id, reservation.getId(), this.externalVariantId,
                this.warehouseId, quantity, type, externalCartId, externalOrderId));

        checkLowStock();
        return reservation;
    }

    /**
     * Release a reservation (cancel, expire).
     */
    public void releaseReservation(StockReservation reservation, String reason) {
        if (!this.reservations.contains(reservation)) {
            throw new IllegalArgumentException("Reservation does not belong to this stock");
        }
        if (reservation.getStatus().isTerminal()) {
            return; // Already released
        }

        int quantity = reservation.getQuantity();
        this.quantityReserved -= quantity;
        this.quantityAvailable += quantity;
        this.updatedAt = LocalDateTime.now();

        if (reservation.isExpired()) {
            reservation.expire();
        } else {
            reservation.cancel();
        }

        registerEvent(new StockReleasedEvent(
                this.id, reservation.getId(), this.externalVariantId,
                this.warehouseId, quantity, reason));
    }

    /**
     * Confirm reservation and convert to actual sale.
     */
    public void confirmReservation(StockReservation reservation, UUID externalOrderId,
            UUID performedById) {
        if (!this.reservations.contains(reservation)) {
            throw new IllegalArgumentException("Reservation does not belong to this stock");
        }

        int quantity = reservation.getQuantity();
        reservation.confirm();

        this.quantityReserved -= quantity;
        this.updatedAt = LocalDateTime.now();

        // Create movement for confirmed sale
        StockMovement movement = StockMovement.builder()
                .stockId(this.id)
                .movementType(MovementType.SALE)
                .quantity(quantity)
                .referenceType("ORDER")
                .externalReferenceId(externalOrderId)
                .externalPerformedById(performedById)
                .note("Reservation confirmed")
                .build();

        this.movements.add(movement);
        reservation.complete();
    }

    /**
     * Update stock thresholds.
     */
    public void updateThresholds(int lowStockThreshold, int reorderPoint) {
        if (lowStockThreshold < 0 || reorderPoint < 0) {
            throw new IllegalArgumentException("Thresholds cannot be negative");
        }
        this.lowStockThreshold = lowStockThreshold;
        this.reorderPoint = reorderPoint;
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Domain Events ==========

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    private void checkLowStock() {
        if (isLowStock()) {
            registerEvent(new LowStockAlertEvent(
                    this.id, this.externalVariantId, this.warehouseId,
                    this.quantityAvailable, this.lowStockThreshold));
        }
    }

    // ========== Builder ==========

    public static class Builder {
        private final Stock stock = new Stock();

        public Builder id(UUID id) {
            stock.id = id;
            return this;
        }

        public Builder externalProductId(UUID id) {
            stock.externalProductId = id;
            return this;
        }

        public Builder externalVariantId(UUID id) {
            stock.externalVariantId = id;
            return this;
        }

        public Builder warehouseId(UUID id) {
            stock.warehouseId = id;
            return this;
        }

        public Builder quantityAvailable(int qty) {
            stock.quantityAvailable = qty;
            return this;
        }

        public Builder quantityReserved(int qty) {
            stock.quantityReserved = qty;
            return this;
        }

        public Builder quantityIncoming(int qty) {
            stock.quantityIncoming = qty;
            return this;
        }

        public Builder lowStockThreshold(int threshold) {
            stock.lowStockThreshold = threshold;
            return this;
        }

        public Builder reorderPoint(int point) {
            stock.reorderPoint = point;
            return this;
        }

        public Builder lastRestockedAt(LocalDateTime time) {
            stock.lastRestockedAt = time;
            return this;
        }

        public Builder createdAt(LocalDateTime time) {
            stock.createdAt = time;
            return this;
        }

        public Builder updatedAt(LocalDateTime time) {
            stock.updatedAt = time;
            return this;
        }

        public Builder movements(List<StockMovement> movements) {
            stock.movements = new ArrayList<>(movements);
            return this;
        }

        public Builder reservations(List<StockReservation> reservations) {
            stock.reservations = new ArrayList<>(reservations);
            return this;
        }

        public Stock build() {
            if (stock.id == null)
                stock.id = UUID.randomUUID();
            if (stock.createdAt == null)
                stock.createdAt = LocalDateTime.now();
            if (stock.updatedAt == null)
                stock.updatedAt = stock.createdAt;
            Objects.requireNonNull(stock.externalVariantId, "Variant ID is required");
            Objects.requireNonNull(stock.warehouseId, "Warehouse ID is required");
            if (stock.quantityAvailable < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }

            // Register creation event for new stocks
            stock.registerEvent(new StockCreatedEvent(
                    stock.id, stock.externalProductId, stock.externalVariantId,
                    stock.warehouseId, stock.quantityAvailable));

            return stock;
        }
    }
}
