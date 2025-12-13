package com.microservice.order.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OrderCoupon entity - represents a coupon applied to an order.
 */
@Getter
@Builder
@AllArgsConstructor
public class OrderCoupon {

    private UUID id;
    private UUID orderId;
    private String couponCode;
    private Money discountAmount;
    private LocalDateTime createdAt;

    public OrderCoupon() {
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
}
