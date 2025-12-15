package com.microservice.inventory.infrastructure.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_movements")
public class StockMovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private StockEntity stock;

    @Column(name = "movement_type_id", nullable = false)
    private String movementType;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "external_reference_id")
    private UUID externalReferenceId;

    @Column(name = "note")
    private String note;

    @Column(name = "external_performed_by_id")
    private UUID externalPerformedById;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StockEntity getStock() {
        return stock;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public UUID getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(UUID externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UUID getExternalPerformedById() {
        return externalPerformedById;
    }

    public void setExternalPerformedById(UUID externalPerformedById) {
        this.externalPerformedById = externalPerformedById;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
