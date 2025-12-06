package com.microservice.shoppingCart.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Item agregado en el carrito.
 */
public class Item {

    private UUID id;
    private UUID shoppingCartId;
    private String externalProductId;
    private String externalVariantId;
    private String externalStoreId;
    private String productName;
    private String variantName;
    private String sku;
    private String imageUrl;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal lineTotal;
    private String externalInventoryReservationId;
    private Instant createdAt;
    private Instant updatedAt;

    public Item() {
        this.id = UUID.randomUUID();
        this.unitPrice = BigDecimal.ZERO;
        this.quantity = 1;
        this.lineTotal = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public Item(UUID id,
                String externalProductId,
                String externalVariantId,
                String productName,
                String variantName,
                String sku,
                BigDecimal unitPrice,
                int quantity) {
        this();
        this.id = id == null ? UUID.randomUUID() : id;
        this.externalProductId = externalProductId;
        this.externalVariantId = externalVariantId;
        this.productName = productName;
        this.variantName = variantName;
        this.sku = sku;
        this.unitPrice = unitPrice == null ? BigDecimal.ZERO : unitPrice;
        this.quantity = quantity <= 0 ? 1 : quantity;
        recalculateLineTotal();
    }

    public void recalculateLineTotal() {
        if (unitPrice == null) {
            this.lineTotal = BigDecimal.ZERO;
        } else {
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        this.updatedAt = Instant.now();
    }

    // Getters / Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(UUID shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    public String getExternalVariantId() {
        return externalVariantId;
    }

    public void setExternalVariantId(String externalVariantId) {
        this.externalVariantId = externalVariantId;
    }

    public String getExternalStoreId() {
        return externalStoreId;
    }

    public void setExternalStoreId(String externalStoreId) {
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
        this.unitPrice = unitPrice == null ? BigDecimal.ZERO : unitPrice;
        recalculateLineTotal();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity <= 0 ? 1 : quantity;
        recalculateLineTotal();
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public String getExternalInventoryReservationId() {
        return externalInventoryReservationId;
    }

    public void setExternalInventoryReservationId(String externalInventoryReservationId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
