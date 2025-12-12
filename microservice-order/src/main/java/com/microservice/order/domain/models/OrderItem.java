package com.microservice.order.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderItem {

    private Long idOrderCoupon;
    private Long idOrder;

    private String couponCode;
    private BigDecimal discountAmount;

    private LocalDateTime createdAt;

    public OrderItem() {
    }
}
