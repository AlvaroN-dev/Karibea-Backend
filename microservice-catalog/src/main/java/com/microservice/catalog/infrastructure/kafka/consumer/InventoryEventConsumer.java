package com.microservice.catalog.infrastructure.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for inventory events.
 * Listens for events from other microservices.
 */
@Component
public class InventoryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventConsumer.class);

    /**
     * Consumes inventory update events.
     * This could be used to update product availability status.
     */
    @KafkaListener(topics = "inventory", groupId = "catalog-service", autoStartup = "false")
    public void consumeInventoryEvent(String message) {
        log.info("Received inventory event: {}", message);
        // Process inventory updates if needed
        // For example, updating stock status on products
    }
}
