package com.microservice.order.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private String orderNumber;
    private Long externalUserProfileId;
    private Long externalStoreId;
    private Long idStatusOrders;
    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal shippingTotal;
    private BigDecimal taxTotal;
    private BigDecimal grandTotal;
    private String currency;
    private Long externalPaymentId;
    private Long externalShipmentId;
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
