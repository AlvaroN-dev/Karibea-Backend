package com.microservice.inventory.infrastructure.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "external_product_id")
    private UUID externalProductId;

    @Column(name = "external_variant_id", nullable = false)
    private UUID externalVariantId;

    @Column(name = "warehouse_id", nullable = false)
    private UUID warehouseId;

    @Column(name = "quantity_available", nullable = false)
    private int quantityAvailable;

    @Column(name = "quantity_reserved", nullable = false)
    private int quantityReserved;

    @Column(name = "quantity_incoming")
    private int quantityIncoming;

    @Column(name = "low_stock_threshold")
    private int lowStockThreshold;

    @Column(name = "reorder_point")
    private int reorderPoint;

    @Column(name = "last_restocked_at")
    private LocalDateTime lastRestockedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<StockMovementEntity> movements = new ArrayList<>();

    @OneToMany(mappedBy = "stock", cascade = CascadeType.PERSIST)
    private List<StockReservationEntity> reservations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(UUID externalProductId) {
        this.externalProductId = externalProductId;
    }

    public UUID getExternalVariantId() {
        return externalVariantId;
    }

    public void setExternalVariantId(UUID externalVariantId) {
        this.externalVariantId = externalVariantId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(int quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public int getQuantityIncoming() {
        return quantityIncoming;
    }

    public void setQuantityIncoming(int quantityIncoming) {
        this.quantityIncoming = quantityIncoming;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public LocalDateTime getLastRestockedAt() {
        return lastRestockedAt;
    }

    public void setLastRestockedAt(LocalDateTime lastRestockedAt) {
        this.lastRestockedAt = lastRestockedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<StockMovementEntity> getMovements() {
        return movements;
    }

    public void setMovements(List<StockMovementEntity> movements) {
        this.movements = movements;
    }

    public List<StockReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<StockReservationEntity> reservations) {
        this.reservations = reservations;
    }
}
