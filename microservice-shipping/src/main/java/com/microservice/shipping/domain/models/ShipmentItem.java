package com.microservice.shipping.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ShipmentItem entity - represents an item being shipped.
 * PURE DOMAIN - No framework dependencies.
 */
public class ShipmentItem {

    private UUID id;
    private UUID shipmentId;
    private UUID externalOrderItemId;
    private UUID externalProductId;
    private String productName;
    private String sku;
    private int quantity;
    private LocalDateTime createdAt;

    private ShipmentItem() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public static ShipmentItem create(
            UUID externalOrderItemId,
            UUID externalProductId,
            String productName,
            String sku,
            int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        ShipmentItem item = new ShipmentItem();
        item.externalOrderItemId = externalOrderItemId;
        item.externalProductId = externalProductId;
        item.productName = productName;
        item.sku = sku;
        item.quantity = quantity;

        return item;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getExternalOrderItemId() {
        return externalOrderItemId;
    }

    public UUID getExternalProductId() {
        return externalProductId;
    }

    public String getProductName() {
        return productName;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Builder for reconstitution
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ShipmentItem item = new ShipmentItem();

        public Builder id(UUID id) {
            item.id = id;
            return this;
        }

        public Builder shipmentId(UUID id) {
            item.shipmentId = id;
            return this;
        }

        public Builder externalOrderItemId(UUID id) {
            item.externalOrderItemId = id;
            return this;
        }

        public Builder externalProductId(UUID id) {
            item.externalProductId = id;
            return this;
        }

        public Builder productName(String name) {
            item.productName = name;
            return this;
        }

        public Builder sku(String sku) {
            item.sku = sku;
            return this;
        }

        public Builder quantity(int qty) {
            item.quantity = qty;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            item.createdAt = dt;
            return this;
        }

        public ShipmentItem build() {
            return item;
        }
    }
}
