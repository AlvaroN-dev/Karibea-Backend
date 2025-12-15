package com.microservice.order.infrastructure.adapters;

import com.microservice.order.domain.events.DomainEvent;
import com.microservice.order.domain.port.out.OrderEventPublisherPort;
import com.microservice.order.infrastructure.kafka.producer.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka adapter for publishing domain events.
 * 
 * This adapter implements the OrderEventPublisherPort interface,
 * delegating to the OrderEventProducer for actual Kafka communication.
 * This follows the Ports & Adapters pattern (Hexagonal Architecture).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderEventPublisher implements OrderEventPublisherPort {

    private final OrderEventProducer orderEventProducer;

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing domain event: {}", event.getEventType());
        orderEventProducer.sendEvent(event);
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        log.debug("Publishing {} domain events", events.size());
        events.forEach(this::publish);
    }
}
