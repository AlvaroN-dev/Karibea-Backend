package com.microservice.shopcart.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA Entity for items table.
 */
@Entity
@Table(name = "items")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_shopping_cart", nullable = false)
    private ShoppingCartEntity shoppingCart;

    @Column(name = "external_product_id", columnDefinition = "uuid")
    private UUID externalProductId;

    @Column(name = "external_variant_id", columnDefinition = "uuid")
    private UUID externalVariantId;

    @Column(name = "external_store_id", columnDefinition = "uuid")
    private UUID externalStoreId;

    @Column(name = "product_name", length = 255)
    private String productName;

    @Column(name = "variant_name", length = 255)
    private String variantName;

    @Column(name = "sku", length = 100)
    private String sku;

    @Column(name = "imagen_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "line_total", precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "external_inventory_reservation_id", columnDefinition = "uuid")
    private UUID externalInventoryReservationId;

    @Column(name = "create_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ShoppingCartEntity getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCartEntity shoppingCart) {
        this.shoppingCart = shoppingCart;
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

    public UUID getExternalStoreId() {
        return externalStoreId;
    }

    public void setExternalStoreId(UUID externalStoreId) {
        this.externalStoreId = externalStoreId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public UUID getExternalInventoryReservationId() {
        return externalInventoryReservationId;
    }

    public void setExternalInventoryReservationId(UUID externalInventoryReservationId) {
        this.externalInventoryReservationId = externalInventoryReservationId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
