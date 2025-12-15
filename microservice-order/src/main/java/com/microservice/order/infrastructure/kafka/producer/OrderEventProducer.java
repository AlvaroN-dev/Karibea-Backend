package com.microservice.order.infrastructure.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka producer for publishing order domain events.
 * 
 * This component handles the serialization and sending of domain events
 * to Kafka topics. It ensures idempotent delivery and proper error handling.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String ORDER_EVENTS_TOPIC = "order-events";

    /**
     * Send a domain event to the order-events topic.
     *
     * @param event The domain event to publish
     * @return CompletableFuture for async result handling
     */
    public CompletableFuture<SendResult<String, String>> sendEvent(DomainEvent event) {
        try {
            String key = event.getAggregateId().toString();
            String payload = objectMapper.writeValueAsString(event);

            log.debug("Publishing event {} with key {}", event.getEventType(), key);

            return kafkaTemplate.send(ORDER_EVENTS_TOPIC, key, payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event: {} - Error: {}",
                                    event.getEventType(), ex.getMessage(), ex);
                        } else {
                            log.info("Event published successfully: {} to topic {} partition {} offset {}",
                                    event.getEventType(),
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Error serializing event: {} - {}", event.getEventType(), e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Send a domain event to a specific topic.
     *
     * @param topic The target topic
     * @param event The domain event to publish
     * @return CompletableFuture for async result handling
     */
    public CompletableFuture<SendResult<String, String>> sendEventToTopic(String topic, DomainEvent event) {
        try {
            String key = event.getAggregateId().toString();
            String payload = objectMapper.writeValueAsString(event);

            log.debug("Publishing event {} to topic {} with key {}", event.getEventType(), topic, key);

            return kafkaTemplate.send(topic, key, payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event to {}: {} - Error: {}",
                                    topic, event.getEventType(), ex.getMessage(), ex);
                        } else {
                            log.info("Event published to {}: {} partition {} offset {}",
                                    topic,
                                    event.getEventType(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Error serializing event for topic {}: {} - {}",
                    topic, event.getEventType(), e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
}
