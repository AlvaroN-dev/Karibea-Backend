package com.microservice.order.domain.events;

import com.microservice.order.domain.models.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event published when a coupon is applied to an order.
 */
@Getter
public class OrderCouponAppliedEvent extends DomainEvent {

    private final String couponCode;
    private final BigDecimal discountAmount;

    private OrderCouponAppliedEvent(UUID orderId, String couponCode, Money discountAmount) {
        super(orderId, "OrderCouponApplied");
        this.couponCode = couponCode;
        this.discountAmount = discountAmount.amount();
    }

    public static OrderCouponAppliedEvent of(UUID orderId, String couponCode, Money discountAmount) {
        return new OrderCouponAppliedEvent(orderId, couponCode, discountAmount);
    }
}
