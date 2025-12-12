package com.microservice.order.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderCoupon {

    private UUID id;
    private UUID orderId;
    private String couponCode;
    private BigDecimal discountAmount;
    private LocalDateTime createdAt;

    public OrderCoupon() {
    }
}
