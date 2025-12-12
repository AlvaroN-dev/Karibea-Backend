package com.microservice.order.domain.ports.out;

import com.microservice.order.domain.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order create(Order newOrder);
    List<Order> getAll();
    Optional<Order> getById(Long idOrder);
    Optional<Order> update(Long id, Order updateOrder);
    Boolean delete(Long idOrder);
}
