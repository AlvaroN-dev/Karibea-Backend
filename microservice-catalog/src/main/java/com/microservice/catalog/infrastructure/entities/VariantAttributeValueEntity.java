package com.microservice.catalog.infrastructure.entities;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * JPA Entity for VariantAttributeValue (junction table).
 */
@Entity
@Table(name = "variante_attribute_values")
public class VariantAttributeValueEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private VariantEntity variant;

    @Column(name = "value_id", nullable = false)
    private UUID valueId;

    // Constructors
    public VariantAttributeValueEntity() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VariantEntity getVariant() {
        return variant;
    }

    public void setVariant(VariantEntity variant) {
        this.variant = variant;
    }

    public UUID getValueId() {
        return valueId;
    }

    public void setValueId(UUID valueId) {
        this.valueId = valueId;
    }
}
