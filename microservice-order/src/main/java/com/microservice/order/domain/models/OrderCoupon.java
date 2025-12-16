package com.microservice.order.domain.models;

import com.microservice.order.domain.models.records.Money;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OrderCoupon entity - represents a coupon applied to an order.
 * 
 * PURE DOMAIN - No framework dependencies.
 */
public class OrderCoupon {

    private UUID id;
    private UUID orderId;
    private String couponCode;
    private Money discountAmount;
    private LocalDateTime createdAt;

    /**
     * Private constructor - use factory method.
     */
    private OrderCoupon() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Factory method to create a new OrderCoupon.
     */
    public static OrderCoupon create(String couponCode, Money discountAmount) {
        if (couponCode == null || couponCode.isBlank()) {
            throw new IllegalArgumentException("Coupon code cannot be blank");
        }
        if (discountAmount == null || discountAmount.isZero()) {
            throw new IllegalArgumentException("Discount amount must be greater than zero");
        }

        OrderCoupon coupon = new OrderCoupon();
        coupon.couponCode = couponCode.toUpperCase();
        coupon.discountAmount = discountAmount;

        return coupon;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    // ========== Getters (Pure Java) ==========

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ========== Builder for Reconstitution ==========

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final OrderCoupon coupon = new OrderCoupon();

        public Builder id(UUID id) {
            coupon.id = id;
            return this;
        }

        public Builder orderId(UUID orderId) {
            coupon.orderId = orderId;
            return this;
        }

        public Builder couponCode(String code) {
            coupon.couponCode = code;
            return this;
        }

        public Builder discountAmount(Money discount) {
            coupon.discountAmount = discount;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            coupon.createdAt = dt;
            return this;
        }

        public OrderCoupon build() {
            return coupon;
        }
    }
}
