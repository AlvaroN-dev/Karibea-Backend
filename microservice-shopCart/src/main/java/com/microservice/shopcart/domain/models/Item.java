package com.microservice.shopcart.domain.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing an item in the shopping cart.
 * Contains denormalized product information from external services.
 */
public class Item {
    
    private UUID id;
    private UUID externalProductId;
    private UUID externalVariantId;
    private UUID externalStoreId;
    private String productName;
    private String variantName;
    private String sku;
    private String imageUrl;
    private Money unitPrice;
    private Quantity quantity;
    private Money lineTotal;
    private UUID externalInventoryReservationId;
    private Instant createdAt;
    private Instant updatedAt;
    
    // Private constructor - use builder
    private Item() {}
    
    public static Builder builder() {
        return new Builder();
    }
    
    public void updateQuantity(Quantity newQuantity) {
        Objects.requireNonNull(newQuantity, "Quantity cannot be null");
        this.quantity = newQuantity;
        this.lineTotal = this.unitPrice.multiply(newQuantity.getValue());
        this.updatedAt = Instant.now();
    }
    
    public void recalculateLineTotal() {
        this.lineTotal = this.unitPrice.multiply(this.quantity.getValue());
    }
    
    // Getters
    public UUID getId() {
        return id;
    }
    
    public UUID getExternalProductId() {
        return externalProductId;
    }
    
    public UUID getExternalVariantId() {
        return externalVariantId;
    }
    
    public UUID getExternalStoreId() {
        return externalStoreId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getVariantName() {
        return variantName;
    }
    
    public String getSku() {
        return sku;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public Money getUnitPrice() {
        return unitPrice;
    }
    
    public Quantity getQuantity() {
        return quantity;
    }
    
    public Money getLineTotal() {
        return lineTotal;
    }
    
    public UUID getExternalInventoryReservationId() {
        return externalInventoryReservationId;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters for internal use
    void setId(UUID id) {
        this.id = id;
    }
    
    void setExternalInventoryReservationId(UUID reservationId) {
        this.externalInventoryReservationId = reservationId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(id, item.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // Builder class
    public static class Builder {
        private final Item item = new Item();
        
        public Builder id(UUID id) {
            item.id = id;
            return this;
        }
        
        public Builder externalProductId(UUID externalProductId) {
            item.externalProductId = externalProductId;
            return this;
        }
        
        public Builder externalVariantId(UUID externalVariantId) {
            item.externalVariantId = externalVariantId;
            return this;
        }
        
        public Builder externalStoreId(UUID externalStoreId) {
            item.externalStoreId = externalStoreId;
            return this;
        }
        
        public Builder productName(String productName) {
            item.productName = productName;
            return this;
        }
        
        public Builder variantName(String variantName) {
            item.variantName = variantName;
            return this;
        }
        
        public Builder sku(String sku) {
            item.sku = sku;
            return this;
        }
        
        public Builder imageUrl(String imageUrl) {
            item.imageUrl = imageUrl;
            return this;
        }
        
        public Builder unitPrice(Money unitPrice) {
            item.unitPrice = unitPrice;
            return this;
        }
        
        public Builder quantity(Quantity quantity) {
            item.quantity = quantity;
            return this;
        }
        
        public Builder lineTotal(Money lineTotal) {
            item.lineTotal = lineTotal;
            return this;
        }
        
        public Builder externalInventoryReservationId(UUID reservationId) {
            item.externalInventoryReservationId = reservationId;
            return this;
        }
        
        public Builder createdAt(Instant createdAt) {
            item.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(Instant updatedAt) {
            item.updatedAt = updatedAt;
            return this;
        }
        
        public Item build() {
            Objects.requireNonNull(item.externalProductId, "Product ID is required");
            Objects.requireNonNull(item.productName, "Product name is required");
            Objects.requireNonNull(item.unitPrice, "Unit price is required");
            Objects.requireNonNull(item.quantity, "Quantity is required");
            
            if (item.id == null) {
                item.id = UUID.randomUUID();
            }
            if (item.createdAt == null) {
                item.createdAt = Instant.now();
            }
            if (item.updatedAt == null) {
                item.updatedAt = Instant.now();
            }
            if (item.lineTotal == null) {
                item.lineTotal = item.unitPrice.multiply(item.quantity.getValue());
            }
            
            return item;
        }
    }
}
