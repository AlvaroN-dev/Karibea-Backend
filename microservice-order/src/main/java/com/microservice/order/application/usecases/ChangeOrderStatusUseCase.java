package com.microservice.order.application.usecases;

import com.microservice.order.application.exception.OrderNotFoundException;
import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.port.in.ChangeOrderStatusPort;
import com.microservice.order.domain.port.out.OrderEventPublisherPort;
import com.microservice.order.domain.port.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for changing order status.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChangeOrderStatusUseCase implements ChangeOrderStatusPort {

    private final OrderRepositoryPort orderRepository;
    private final OrderEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public void execute(ChangeOrderStatusCommand command) {
        log.info("Changing order {} status to: {}", command.orderId(), command.newStatus());

        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> OrderNotFoundException.withId(command.orderId()));

        switch (command.newStatus()) {
            case CONFIRMED -> order.confirm(command.relatedEntityId(), command.changedBy());
            case PROCESSING -> order.process(command.changedBy());
            case SHIPPED -> order.ship(command.relatedEntityId(), command.changedBy());
            case DELIVERED -> order.deliver(command.changedBy());
            case COMPLETED -> order.complete(command.changedBy());
            case CANCELLED -> order.cancel(command.reason(), command.changedBy());
            case RETURNED -> order.initiateReturn(command.reason(), command.changedBy());
            case REFUNDED -> order.refund(command.changedBy());
            default -> throw new IllegalArgumentException("Unsupported status transition: " + command.newStatus());
        }

        orderRepository.save(order);
        eventPublisher.publishAll(order.getDomainEvents());
        order.clearDomainEvents();

        log.info("Order {} status changed to: {}", order.getOrderNumber(), command.newStatus());
    }
}
