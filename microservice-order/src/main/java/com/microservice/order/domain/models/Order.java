package com.microservice.order.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;
    private String orderNumber;

    private UUID externalUserId;
    private UUID externalStoreId;
    private UUID externalShipmentId;
    private UUID externalPaymentId;

    private UUID statusId;

    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal shippingTotal;
    private BigDecimal taxTotal;
    private BigDecimal grandTotal;

    private String currency;
    private String shippingAddress;
    private String billingAddress;

    private String notes;
    private String customerNotes;
    private String ipAddress;
    private String userAgent;

    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItem> items;
    private List<OrderCoupon> coupons;

    public Order() {
    }






}
