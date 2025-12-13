package com.microservice.order.domain.ports.in;

import com.microservice.order.domain.models.Order;

import java.util.Optional;

public interface UpdateOrderUseCase {
    Optional<Order> updateOrder(Long id, Order updateOrder);
}
