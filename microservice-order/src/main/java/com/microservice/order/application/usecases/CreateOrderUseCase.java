package com.microservice.order.application.usecases;

import com.microservice.order.domain.models.records.Address;
import com.microservice.order.domain.models.records.Money;
import com.microservice.order.domain.models.Order;
import com.microservice.order.domain.models.OrderItem;
import com.microservice.order.domain.port.in.CreateOrderPort;
import com.microservice.order.domain.port.out.OrderEventPublisherPort;
import com.microservice.order.domain.port.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use case for creating orders.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOrderUseCase implements CreateOrderPort {

        private final OrderRepositoryPort orderRepository;
        private final OrderEventPublisherPort eventPublisher;

        @Override
        @Transactional
        public Order execute(CreateOrderCommand command) {
                log.info("Creating order for customer: {}", command.customerId());

                // Build addresses
                Address shippingAddress = Address.of(
                                command.shippingStreet(),
                                command.shippingCity(),
                                command.shippingState(),
                                command.shippingZipCode(),
                                command.shippingCountry());

                Address billingAddress = command.billingStreet() != null
                                ? Address.of(
                                                command.billingStreet(),
                                                command.billingCity(),
                                                command.billingState(),
                                                command.billingZipCode(),
                                                command.billingCountry())
                                : shippingAddress;

                // Build order items
                List<OrderItem> items = command.items().stream()
                                .map(this::toOrderItem)
                                .toList();

                // Create order aggregate
                Order order = Order.create(
                                command.customerId(),
                                command.storeId(),
                                command.currency(),
                                shippingAddress,
                                billingAddress,
                                items);

                // Set metadata
                order.setMetadata(command.ipAddress(), command.userAgent());
                order.setCustomerNotes(command.customerNotes());

                // Persist
                Order savedOrder = orderRepository.save(order);

                // Publish domain events
                eventPublisher.publishAll(savedOrder.getDomainEvents());
                savedOrder.clearDomainEvents();

                log.info("Order created successfully: {}", savedOrder.getOrderNumber());

                return savedOrder;
        }

        private OrderItem toOrderItem(OrderItemCommand cmd) {
                return OrderItem.create(
                                cmd.productId(),
                                cmd.productName(),
                                cmd.variantName(),
                                cmd.sku(),
                                cmd.imageUrl(),
                                Money.of(cmd.unitPrice()),
                                cmd.quantity());
        }
}
