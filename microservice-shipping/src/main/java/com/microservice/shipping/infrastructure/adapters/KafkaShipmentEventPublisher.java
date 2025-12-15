package com.microservice.shipping.infrastructure.adapters;

import com.microservice.shipping.domain.events.DomainEvent;
import com.microservice.shipping.domain.port.out.ShipmentEventPublisherPort;
import com.microservice.shipping.infrastructure.kafka.producer.ShipmentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapter that implements the domain port for event publishing.
 * Delegates to the Kafka producer for actual event delivery.
 * 
 * This adapter follows the Hexagonal Architecture pattern, providing
 * a clean separation between the domain and infrastructure layers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaShipmentEventPublisher implements ShipmentEventPublisherPort {

    private final ShipmentEventProducer shipmentEventProducer;

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing domain event: {}", event.getEventType());
        shipmentEventProducer.publish(event);
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            log.debug("No events to publish");
            return;
        }
        log.debug("Publishing {} domain events", events.size());
        shipmentEventProducer.publishAll(events);
    }
}
