package com.microservice.order.domain.port.in;

import com.microservice.order.domain.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Inbound port for querying orders.
 */
public interface GetOrderPort {

    Optional<Order> findById(UUID orderId);

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByCustomerId(UUID customerId, Pageable pageable);

    Page<Order> findByStoreId(UUID storeId, Pageable pageable);
}
