package com.microservice.order.application.usecases;

import com.microservice.order.application.exception.OrderNotFoundException;
import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.port.in.ConfirmOrderPort;
import com.microservice.order.domain.port.out.OrderEventPublisherPort;
import com.microservice.order.domain.port.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for confirming orders after payment.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmOrderUseCase implements ConfirmOrderPort {

    private final OrderRepositoryPort orderRepository;
    private final OrderEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public void execute(ConfirmOrderCommand command) {
        log.info("Confirming order: {} with payment: {}", command.orderId(), command.paymentId());

        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> OrderNotFoundException.withId(command.orderId()));

        order.confirm(command.paymentId(), command.confirmedBy());

        orderRepository.save(order);
        eventPublisher.publishAll(order.getDomainEvents());
        order.clearDomainEvents();

        log.info("Order confirmed: {}", order.getOrderNumber());
    }
}
