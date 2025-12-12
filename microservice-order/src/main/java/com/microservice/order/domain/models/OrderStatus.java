package com.microservice.order.domain.models;

import java.time.LocalDateTime;

public class OrderStatus    {

    private Long idStatusOrders;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public OrderStatus() {
    }
}
