package com.microservice.order.domain.ports.in;

import com.microservice.order.domain.models.Order;

import java.util.List;
import java.util.Optional;

public interface GetOrderUseCase {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
}
