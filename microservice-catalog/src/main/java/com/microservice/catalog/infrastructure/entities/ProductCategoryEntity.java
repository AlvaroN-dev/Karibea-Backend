package com.microservice.catalog.infrastructure.entities;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * JPA Entity for ProductCategory (junction table).
 */
@Entity
@Table(name = "product_categories")
public class ProductCategoryEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    // Constructors
    public ProductCategoryEntity() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
