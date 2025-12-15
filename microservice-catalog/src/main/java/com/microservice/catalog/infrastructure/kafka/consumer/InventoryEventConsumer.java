package com.microservice.catalog.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kafka consumer for inventory events.
 * Listens for inventory events to update product availability status.
 * 
 * Consumed Topics:
 * - inventory.updated: When stock levels change
 * - inventory.low-stock: When stock falls below threshold
 * - inventory.out-of-stock: When product is out of stock
 */
@Component
public class InventoryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventConsumer.class);
    private final ObjectMapper objectMapper;

    public InventoryEventConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Consumes inventory update events to sync product availability.
     * Event structure:
     * {
     *   "eventType": "inventory.updated",
     *   "variantId": "uuid",
     *   "sku": "PROD-001-M-RED",
     *   "quantity": 100,
     *   "available": true,
     *   "timestamp": "2024-01-15T10:30:00Z"
     * }
     */
    @KafkaListener(
            topics = "inventory.updated",
            groupId = "catalog-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeInventoryUpdatedEvent(String message) {
        log.info("Received inventory.updated event: {}", message);
        try {
            JsonNode event = objectMapper.readTree(message);
            UUID variantId = UUID.fromString(event.path("variantId").asText());
            int quantity = event.path("quantity").asInt();
            boolean available = event.path("available").asBoolean(true);

            log.info("Processing inventory update: variantId={}, quantity={}, available={}",
                    variantId, quantity, available);

            // TODO: Update variant availability status in catalog if needed
            // This could trigger a product status change event

        } catch (JsonProcessingException e) {
            log.error("Failed to parse inventory.updated event: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing inventory.updated event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consumes low stock alerts to potentially mark products as limited.
     * Event structure:
     * {
     *   "eventType": "inventory.low-stock",
     *   "variantId": "uuid",
     *   "sku": "PROD-001-M-RED",
     *   "currentQuantity": 5,
     *   "threshold": 10,
     *   "timestamp": "2024-01-15T10:30:00Z"
     * }
     */
    @KafkaListener(
            topics = "inventory.low-stock",
            groupId = "catalog-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeLowStockEvent(String message) {
        log.info("Received inventory.low-stock event: {}", message);
        try {
            JsonNode event = objectMapper.readTree(message);
            UUID variantId = UUID.fromString(event.path("variantId").asText());
            String sku = event.path("sku").asText();
            int currentQuantity = event.path("currentQuantity").asInt();
            int threshold = event.path("threshold").asInt();

            log.warn("Low stock alert: variantId={}, sku={}, quantity={}, threshold={}",
                    variantId, sku, currentQuantity, threshold);

            // TODO: Could trigger notification or mark product as "limited stock"

        } catch (JsonProcessingException e) {
            log.error("Failed to parse inventory.low-stock event: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing inventory.low-stock event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consumes out of stock events to update product availability.
     * Event structure:
     * {
     *   "eventType": "inventory.out-of-stock",
     *   "variantId": "uuid",
     *   "sku": "PROD-001-M-RED",
     *   "productId": "uuid",
     *   "timestamp": "2024-01-15T10:30:00Z"
     * }
     */
    @KafkaListener(
            topics = "inventory.out-of-stock",
            groupId = "catalog-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOutOfStockEvent(String message) {
        log.info("Received inventory.out-of-stock event: {}", message);
        try {
            JsonNode event = objectMapper.readTree(message);
            UUID variantId = UUID.fromString(event.path("variantId").asText());
            String sku = event.path("sku").asText();
            UUID productId = event.has("productId") ? 
                    UUID.fromString(event.path("productId").asText()) : null;

            log.warn("Out of stock: variantId={}, sku={}, productId={}", 
                    variantId, sku, productId);

            // TODO: Update variant status to unavailable
            // Could also check if all variants are out of stock to deactivate product

        } catch (JsonProcessingException e) {
            log.error("Failed to parse inventory.out-of-stock event: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing inventory.out-of-stock event: {}", e.getMessage(), e);
        }
    }
}
