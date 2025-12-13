package com.microservice.order.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OrderItem entity - child of Order aggregate.
 * Represents a product line in an order.
 */
@Getter
@Builder
@AllArgsConstructor
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

    public OrderItem() {
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
}
