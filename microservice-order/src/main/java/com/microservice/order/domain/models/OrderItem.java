package com.microservice.order.domain.models;

import com.microservice.order.domain.models.records.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OrderItem entity - child of Order aggregate.
 * Represents a product line in an order.
 * 
 * PURE DOMAIN - No framework dependencies.
 */
public class OrderItem {

    private UUID id;
    private UUID orderId;
    private UUID externalProductId;

    // Product snapshot at time of order
    private String productName;
    private String variantName;
    private String sku;
    private String imageUrl;

    // Pricing
    private Money unitPrice;
    private int quantity;
    private Money discountAmount;
    private Money taxAmount;
    private Money lineTotal;

    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Private constructor - use factory method.
     */
    private OrderItem() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Factory method to create a new OrderItem.
     */
    public static OrderItem create(
            UUID externalProductId,
            String productName,
            String variantName,
            String sku,
            String imageUrl,
            Money unitPrice,
            int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        OrderItem item = new OrderItem();
        item.externalProductId = externalProductId;
        item.productName = productName;
        item.variantName = variantName;
        item.sku = sku;
        item.imageUrl = imageUrl;
        item.unitPrice = unitPrice;
        item.quantity = quantity;
        item.discountAmount = Money.zero();
        item.taxAmount = Money.zero();
        item.status = "ACTIVE";
        item.calculateLineTotal();

        return item;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void applyDiscount(Money discount) {
        this.discountAmount = discount;
        this.updatedAt = LocalDateTime.now();
        calculateLineTotal();
    }

    public void applyTax(Money tax) {
        this.taxAmount = tax;
        this.updatedAt = LocalDateTime.now();
        calculateLineTotal();
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = newQuantity;
        this.updatedAt = LocalDateTime.now();
        calculateLineTotal();
    }

    private void calculateLineTotal() {
        BigDecimal total = unitPrice.amount()
                .multiply(BigDecimal.valueOf(quantity))
                .subtract(discountAmount.amount())
                .add(taxAmount.amount());

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }
        this.lineTotal = Money.of(total);
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Getters (Pure Java) ==========

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getExternalProductId() {
        return externalProductId;
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

    public int getQuantity() {
        return quantity;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public Money getTaxAmount() {
        return taxAmount;
    }

    public Money getLineTotal() {
        return lineTotal;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ========== Builder for Reconstitution ==========

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final OrderItem item = new OrderItem();

        public Builder id(UUID id) {
            item.id = id;
            return this;
        }

        public Builder orderId(UUID orderId) {
            item.orderId = orderId;
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

        public Builder variantName(String name) {
            item.variantName = name;
            return this;
        }

        public Builder sku(String sku) {
            item.sku = sku;
            return this;
        }

        public Builder imageUrl(String url) {
            item.imageUrl = url;
            return this;
        }

        public Builder unitPrice(Money price) {
            item.unitPrice = price;
            return this;
        }

        public Builder quantity(int qty) {
            item.quantity = qty;
            return this;
        }

        public Builder discountAmount(Money discount) {
            item.discountAmount = discount;
            return this;
        }

        public Builder taxAmount(Money tax) {
            item.taxAmount = tax;
            return this;
        }

        public Builder lineTotal(Money total) {
            item.lineTotal = total;
            return this;
        }

        public Builder status(String status) {
            item.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            item.createdAt = dt;
            return this;
        }

        public Builder updatedAt(LocalDateTime dt) {
            item.updatedAt = dt;
            return this;
        }

        public OrderItem build() {
            return item;
        }
    }
}
