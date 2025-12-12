package com.microservice.order.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderCoupon {

    private Long idOrderItem;
    private Long idOrder;

    private Long externalProductId;
    private String productName;
    private String variantName;
    private String sku;
    private String imageUrl;

    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal lineTotal;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderCoupon() {
    }
}
