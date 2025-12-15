package com.microservice.payment.infrastructure.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.payment.infrastructure.kafka.config.KafkaTopics;

/**
 * Kafka consumer for order-related events.
 * Processes events from the Order service to trigger payments.
 */
@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final ObjectMapper objectMapper;

    public OrderEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = KafkaTopics.ORDER_EVENTS, groupId = "${spring.kafka.consumer.group-id:payment-service}")
    public void handleOrderEvent(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            String eventType = event.path("eventType").asText();

            log.info("Received order event: {}", eventType);

            switch (eventType) {
                case "order.created" -> handleOrderCreated(event);
                case "order.cancelled" -> handleOrderCancelled(event);
                default -> log.warn("Unknown order event type: {}", eventType);
            }

        } catch (Exception e) {
            log.error("Failed to process order event", e);
        }
    }

    private void handleOrderCreated(JsonNode event) {
        // Process order created event
        // Could trigger automatic payment processing
        log.info("Order created event received: {}", event.path("orderId").asText());
    }

    private void handleOrderCancelled(JsonNode event) {
        // Process order cancelled event
        // Could trigger automatic refund
        log.info("Order cancelled event received: {}", event.path("orderId").asText());
    }
}
