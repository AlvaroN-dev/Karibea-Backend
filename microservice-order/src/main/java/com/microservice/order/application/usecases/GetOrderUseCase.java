package com.microservice.order.application.usecases;

import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.port.in.GetOrderPort;
import com.microservice.order.domain.port.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case for querying orders.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GetOrderUseCase implements GetOrderPort {

    private final OrderRepositoryPort orderRepository;

    @Override
    public Optional<Order> findById(UUID orderId) {
        log.debug("Finding order by id: {}", orderId);
        return orderRepository.findById(orderId);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        log.debug("Finding order by number: {}", orderNumber);
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public Page<Order> findByCustomerId(UUID customerId, Pageable pageable) {
        log.debug("Finding orders for customer: {}", customerId);
        return orderRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    public Page<Order> findByStoreId(UUID storeId, Pageable pageable) {
        log.debug("Finding orders for store: {}", storeId);
        return orderRepository.findByStoreId(storeId, pageable);
    }
}
