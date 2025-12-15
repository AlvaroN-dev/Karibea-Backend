package com.microservice.order.infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.port.in.CancelOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Kafka consumer for inventory events.
 * 
 * Handles inventory-related events from the inventory microservice:
 * - InventoryReserved: Stock has been reserved for the order
 * - InventoryReservationFailed: Stock reservation failed (out of stock)
 * - InventoryReleased: Reserved stock has been released
 * 
 * This consumer follows event-driven architecture principles,
 * ensuring loose coupling between order and inventory services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final CancelOrderPort cancelOrderPort;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "inventory-events",
            groupId = "${spring.kafka.consumer.group-id:order-service}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleInventoryEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            Acknowledgment ack) {

        try {
            log.info("Received inventory event with key: {}", key);
            log.debug("Inventory event payload: {}", payload);

            JsonNode event = objectMapper.readTree(payload);
            String eventType = event.get("eventType").asText();
            String eventId = event.has("eventId") ? event.get("eventId").asText() : "unknown";

            log.info("Processing inventory event: {} (eventId: {})", eventType, eventId);

            switch (eventType) {
                case "InventoryReserved" -> handleInventoryReserved(event);
                case "InventoryReservationFailed" -> handleInventoryReservationFailed(event);
                case "InventoryReleased" -> handleInventoryReleased(event);
                default -> log.warn("Unknown inventory event type: {}. Ignoring.", eventType);
            }

            ack.acknowledge();
            log.info("Inventory event {} processed successfully", eventType);

        } catch (Exception ex) {
            log.error("Error processing inventory event: {}", ex.getMessage(), ex);
            // Don't acknowledge - message will be retried based on error handler configuration
        }
    }

    /**
     * Handle inventory reserved - stock has been reserved for the order.
     */
    private void handleInventoryReserved(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());

        log.info("Inventory reserved successfully for order: {}", orderId);
        // Order flow continues - payment can now proceed
        // No state change needed here, just logging for audit
    }

    /**
     * Handle inventory reservation failed - cancels the order due to stock unavailability.
     */
    private void handleInventoryReservationFailed(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        String reason = event.has("reason") ? event.get("reason").asText() : "Insufficient stock";

        log.warn("Inventory reservation failed for order: {} - Reason: {}", orderId, reason);

        cancelOrderPort.execute(new CancelOrderPort.CancelOrderCommand(
                orderId,
                "Inventory reservation failed: " + reason,
                "inventory-service"
        ));

        log.info("Order {} cancelled due to inventory reservation failure", orderId);
    }

    /**
     * Handle inventory released - reserved stock has been released.
     */
    private void handleInventoryReleased(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());

        log.info("Inventory released for order: {}", orderId);
        // This is informational - typically happens after order cancellation
        // No state change needed, just logging for audit
    }
}
