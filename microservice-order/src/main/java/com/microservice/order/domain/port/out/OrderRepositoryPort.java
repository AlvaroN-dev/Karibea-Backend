package com.microservice.order.domain.port.out;

import com.microservice.order.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for Order persistence.
 */
public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByCustomerId(UUID customerId, Pageable pageable);

    Page<Order> findByStoreId(UUID storeId, Pageable pageable);

    boolean existsByOrderNumber(String orderNumber);

    void deleteById(UUID id);
}
