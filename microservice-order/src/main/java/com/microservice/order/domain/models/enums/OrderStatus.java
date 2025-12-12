package com.microservice.order.domain.models.enums;

import java.time.LocalDateTime;

public enum OrderStatus{
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REJECTED
}
