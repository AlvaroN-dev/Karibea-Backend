package com.microservice.order.domain.ports.in;

import com.microservice.order.domain.models.Order;

public interface CreateOrderUseCase {
    Order createNewOrder(Order newOrder);
}
