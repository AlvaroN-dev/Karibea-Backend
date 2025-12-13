package com.microservice.shipping.infrastructure.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.shipping.domain.events.DomainEvent;
import com.microservice.shipping.domain.port.out.ShipmentEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka adapter for publishing domain events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaShipmentEventPublisher implements ShipmentEventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String SHIPMENT_EVENTS_TOPIC = "shipment-events";

    @Override
    public void publish(DomainEvent event) {
        try {
            String key = event.getAggregateId().toString();
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(SHIPMENT_EVENTS_TOPIC, key, payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event: {} - {}", event.getEventType(), ex.getMessage());
                        } else {
                            log.info("Event published: {} to partition {}",
                                    event.getEventType(),
                                    result.getRecordMetadata().partition());
                        }
                    });
        } catch (Exception e) {
            log.error("Error serializing event: {}", event.getEventType(), e);
        }
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
