package com.microservice.order.domain.models;

import java.time.LocalDateTime;

public class OrderStatusHistory {

    private Long idStatusHistory;
    private Long idOrder;

    private String previousStatus;
    private String newStatus;

    private String changedBy;
    private String changedReason;
    private String metadata;

    private LocalDateTime createdAt;

    public OrderStatusHistory() {
    }
}
