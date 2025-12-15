package com.microservice.shipping.infrastructure.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.shipping.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

/**
 * Kafka producer for shipment domain events.
 * Centralizes all event publishing logic with proper headers and error handling.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String SHIPMENT_EVENTS_TOPIC = "shipment-events";

    /**
     * Publishes a single domain event to Kafka.
     *
     * @param event The domain event to publish
     */
    public void publish(DomainEvent event) {
        try {
            String key = event.getAggregateId().toString();
            String payload = objectMapper.writeValueAsString(event);

            ProducerRecord<String, String> record = new ProducerRecord<>(SHIPMENT_EVENTS_TOPIC, key, payload);

            // Add headers for traceability
            record.headers().add(new RecordHeader("eventType", event.getEventType().getBytes(StandardCharsets.UTF_8)));
            record.headers().add(new RecordHeader("timestamp", Instant.now().toString().getBytes(StandardCharsets.UTF_8)));
            record.headers().add(new RecordHeader("source", "microservice-shipping".getBytes(StandardCharsets.UTF_8)));

            kafkaTemplate.send(record)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event [{}] for shipment [{}]: {}",
                                    event.getEventType(), key, ex.getMessage());
                        } else {
                            log.info("Event [{}] published for shipment [{}] to partition [{}] at offset [{}]",
                                    event.getEventType(),
                                    key,
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Error serializing event [{}]: {}", event.getEventType(), e.getMessage(), e);
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    /**
     * Publishes multiple domain events to Kafka.
     *
     * @param events The list of domain events to publish
     */
    public void publishAll(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        events.forEach(this::publish);
    }
}
