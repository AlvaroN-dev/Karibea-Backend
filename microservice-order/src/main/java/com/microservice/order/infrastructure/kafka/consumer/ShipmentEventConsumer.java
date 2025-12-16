package com.microservice.order.infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.models.enums.OrderStatusEnum;
import com.microservice.order.domain.port.in.ChangeOrderStatusPort;
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
 * Kafka consumer for shipment events.
 * 
 * Handles shipment-related events from the shipping microservice:
 * - ShipmentCreated: Updates order with shipment ID
 * - ShipmentShipped: Updates order status to shipped
 * - ShipmentDelivered: Updates order status to delivered
 * - ShipmentReturned: Updates order status to returned
 * 
 * This consumer follows event-driven architecture principles,
 * ensuring loose coupling between order and shipping services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventConsumer {

    private final ChangeOrderStatusPort changeOrderStatusPort;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "shipment-events",
            groupId = "${spring.kafka.consumer.group-id:order-service}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleShipmentEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            Acknowledgment ack) {

        try {
            log.info("Received shipment event with key: {}", key);
            log.debug("Shipment event payload: {}", payload);

            JsonNode event = objectMapper.readTree(payload);
            String eventType = event.get("eventType").asText();
            String eventId = event.has("eventId") ? event.get("eventId").asText() : "unknown";

            log.info("Processing shipment event: {} (eventId: {})", eventType, eventId);

            switch (eventType) {
                case "ShipmentCreated" -> handleShipmentCreated(event);
                case "ShipmentShipped" -> handleShipmentShipped(event);
                case "ShipmentDelivered" -> handleShipmentDelivered(event);
                case "ShipmentReturned" -> handleShipmentReturned(event);
                default -> log.warn("Unknown shipment event type: {}. Ignoring.", eventType);
            }

            ack.acknowledge();
            log.info("Shipment event {} processed successfully", eventType);

        } catch (Exception ex) {
            log.error("Error processing shipment event: {}", ex.getMessage(), ex);
            // Don't acknowledge - message will be retried based on error handler configuration
        }
    }

    /**
     * Handle shipment created - updates order with shipment reference.
     */
    private void handleShipmentCreated(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        UUID shipmentId = UUID.fromString(event.get("shipmentId").asText());

        log.info("Processing ShipmentCreated for order: {} with shipment: {}", orderId, shipmentId);

        // Order moves to PROCESSING when shipment is created
        changeOrderStatusPort.execute(new ChangeOrderStatusPort.ChangeOrderStatusCommand(
                orderId,
                OrderStatusEnum.PROCESSING,
                "Shipment created",
                "shipping-service",
                shipmentId
        ));

        log.info("Order {} updated to PROCESSING with shipment {}", orderId, shipmentId);
    }

    /**
     * Handle shipment shipped - updates order status to shipped.
     */
    private void handleShipmentShipped(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        UUID shipmentId = UUID.fromString(event.get("shipmentId").asText());
        String trackingNumber = event.has("trackingNumber") ? event.get("trackingNumber").asText() : null;

        log.info("Processing ShipmentShipped for order: {} - Tracking: {}", orderId, trackingNumber);

        changeOrderStatusPort.execute(new ChangeOrderStatusPort.ChangeOrderStatusCommand(
                orderId,
                OrderStatusEnum.SHIPPED,
                "Shipment shipped" + (trackingNumber != null ? " - Tracking: " + trackingNumber : ""),
                "shipping-service",
                shipmentId
        ));

        log.info("Order {} marked as shipped", orderId);
    }

    /**
     * Handle shipment delivered - updates order status to delivered.
     */
    private void handleShipmentDelivered(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());

        log.info("Processing ShipmentDelivered for order: {}", orderId);

        changeOrderStatusPort.execute(new ChangeOrderStatusPort.ChangeOrderStatusCommand(
                orderId,
                OrderStatusEnum.DELIVERED,
                "Shipment delivered",
                "shipping-service",
                null
        ));

        log.info("Order {} marked as delivered", orderId);
    }

    /**
     * Handle shipment returned - updates order status to returned.
     */
    private void handleShipmentReturned(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        String returnReason = event.has("reason") ? event.get("reason").asText() : "Package returned";

        log.info("Processing ShipmentReturned for order: {} - Reason: {}", orderId, returnReason);

        changeOrderStatusPort.execute(new ChangeOrderStatusPort.ChangeOrderStatusCommand(
                orderId,
                OrderStatusEnum.RETURNED,
                returnReason,
                "shipping-service",
                null
        ));

        log.info("Order {} marked as returned", orderId);
    }
}
