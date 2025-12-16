package com.microservice.shipping.infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.shipping.domain.port.in.CreateShipmentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Kafka consumer for order events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final CreateShipmentPort createShipmentPort;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "shipping-service", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void handleOrderEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            Acknowledgment ack) {

        try {
            log.info("Received order event for key: {}", key);

            JsonNode event = objectMapper.readTree(payload);
            String eventType = event.get("eventType").asText();

            switch (eventType) {
                case "OrderConfirmed" -> handleOrderConfirmed(event);
                case "OrderCancelled" -> handleOrderCancelled(event);
                default -> log.debug("Ignoring order event type: {}", eventType);
            }

            ack.acknowledge();

        } catch (Exception ex) {
            log.error("Error processing order event: {}", ex.getMessage(), ex);
        }
    }

    private void handleOrderConfirmed(JsonNode event) {
        try {
            UUID orderId = UUID.fromString(event.get("aggregateId").asText());
            log.info("Processing OrderConfirmed for order: {}", orderId);

            JsonNode payload = event.has("payload") ? event.get("payload") : event;

            // Extract basic info
            UUID storeId = UUID.fromString(payload.path("storeId").asText());
            UUID customerId = UUID.fromString(payload.path("customerId").asText());

            // Extract shipping structure if exists, otherwise assume flat or mocked for now
            JsonNode shippingInfo = payload.path("shippingInfo");

            // These would likely come from the event or a lookup.
            // Mapping from event for completeness.
            UUID carrierId = hasText(shippingInfo, "carrierId")
                    ? UUID.fromString(shippingInfo.get("carrierId").asText())
                    : null;
            UUID shippingMethodId = hasText(shippingInfo, "shippingMethodId")
                    ? UUID.fromString(shippingInfo.get("shippingMethodId").asText())
                    : null;
            String carrierCode = shippingInfo.path("carrierCode").asText("DEFAULT_CARRIER");
            String carrierName = shippingInfo.path("carrierName").asText("Default Carrier");
            String shippingMethodName = shippingInfo.path("methodName").asText("Standard");
            BigDecimal shippingCost = new BigDecimal(shippingInfo.path("cost").asText("0.00"));

            // Extract Address
            JsonNode address = payload.path("shippingAddress");
            String originStreet = "Warehouse 1"; // Default origin
            String originCity = "Distrib City";
            String originState = "DS";
            String originZipCode = "00000";
            String originCountry = "US";

            String destStreet = address.path("street").asText();
            String destCity = address.path("city").asText();
            String destState = address.path("state").asText();
            String destZipCode = address.path("zipCode").asText();
            String destCountry = address.path("country").asText();

            // Extract Items
            List<CreateShipmentPort.ShipmentItemCommand> items = new ArrayList<>();
            if (payload.has("items")) {
                for (JsonNode itemNode : payload.get("items")) {
                    items.add(new CreateShipmentPort.ShipmentItemCommand(
                            UUID.fromString(itemNode.path("orderItemId").asText()),
                            UUID.fromString(itemNode.path("productId").asText()),
                            itemNode.path("productName").asText(),
                            itemNode.path("sku").asText(),
                            itemNode.path("quantity").asInt()));
                }
            }

            CreateShipmentPort.CreateShipmentCommand command = new CreateShipmentPort.CreateShipmentCommand(
                    orderId,
                    storeId,
                    customerId,
                    carrierId,
                    shippingMethodId,
                    carrierCode,
                    carrierName,
                    shippingMethodName,
                    originStreet,
                    originCity,
                    originState,
                    originZipCode,
                    originCountry,
                    destStreet,
                    destCity,
                    destState,
                    destZipCode,
                    destCountry,
                    shippingCost,
                    items,
                    "Generated from Order Event");

            createShipmentPort.execute(command);
            log.info("Shipment created successfully for order: {}", orderId);

        } catch (Exception e) {
            log.error("Failed to create shipment for order event", e);
            throw new RuntimeException("Shipment creation failed", e);
        }
    }

    private boolean hasText(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() && !node.get(field).asText().isEmpty();
    }

    private void handleOrderCancelled(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("aggregateId").asText());
        log.warn("Order cancelled, may need to cancel pending shipments: {}", orderId);
        // TODO: Cancel pending shipments for this order
    }
}
