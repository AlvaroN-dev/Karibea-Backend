package com.microservice.order.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderStatusHistory {

    private UUID id;
    private UUID orderId;
    private UUID previousStatus;
    private UUID newStatus;
    private String changeReason;
    private LocalDateTime createdAt;

    public OrderStatusHistory() {
    }
}
