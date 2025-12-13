package com.microservice.order.infrastructure.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.events.DomainEvent;
import com.microservice.order.domain.port.out.OrderEventPublisherPort;
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
public class KafkaOrderEventPublisher implements OrderEventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String ORDER_EVENTS_TOPIC = "order-events";

    @Override
    public void publish(DomainEvent event) {
        try {
            String key = event.getAggregateId().toString();
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(ORDER_EVENTS_TOPIC, key, payload)
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
