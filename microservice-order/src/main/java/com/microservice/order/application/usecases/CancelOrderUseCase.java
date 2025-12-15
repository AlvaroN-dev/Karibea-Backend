package com.microservice.order.application.usecases;

import com.microservice.order.application.exception.OrderNotFoundException;
import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.port.in.CancelOrderPort;
import com.microservice.order.domain.port.out.OrderEventPublisherPort;
import com.microservice.order.domain.port.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for cancelling orders.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CancelOrderUseCase implements CancelOrderPort {

    private final OrderRepositoryPort orderRepository;
    private final OrderEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public void execute(CancelOrderCommand command) {
        log.info("Cancelling order: {} - Reason: {}", command.orderId(), command.reason());

        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> OrderNotFoundException.withId(command.orderId()));

        order.cancel(command.reason(), command.cancelledBy());

        orderRepository.save(order);
        eventPublisher.publishAll(order.getDomainEvents());
        order.clearDomainEvents();

        log.info("Order cancelled: {}", order.getOrderNumber());
    }
}
