package com.microservice.order.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderItem {

    private UUID id;
    private UUID externalProductId;
    private String productName;
    private String variantName;

    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private Integer quantity;

    private String sku;
    private String imageUrl;

    private LocalDateTime createdAt;

    public OrderItem() {
    }
}
