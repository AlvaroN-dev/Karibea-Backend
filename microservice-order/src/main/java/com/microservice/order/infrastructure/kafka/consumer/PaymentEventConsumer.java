package com.microservice.order.infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.port.in.ConfirmOrderPort;
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
 * Kafka consumer for payment events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final ConfirmOrderPort confirmOrderPort;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-events", groupId = "order-service", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void handlePaymentEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            Acknowledgment ack) {

        try {
            log.info("Received payment event for key: {}", key);

            JsonNode event = objectMapper.readTree(payload);
            String eventType = event.get("eventType").asText();

            switch (eventType) {
                case "PaymentCompleted" -> handlePaymentCompleted(event);
                case "PaymentFailed" -> handlePaymentFailed(event);
                case "PaymentRefunded" -> handlePaymentRefunded(event);
                default -> log.warn("Unknown payment event type: {}", eventType);
            }

            ack.acknowledge();

        } catch (Exception ex) {
            log.error("Error processing payment event: {}", ex.getMessage(), ex);
            // Don't acknowledge - will be retried
        }
    }

    private void handlePaymentCompleted(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        UUID paymentId = UUID.fromString(event.get("paymentId").asText());

        log.info("Processing PaymentCompleted for order: {}", orderId);

        confirmOrderPort.execute(new ConfirmOrderPort.ConfirmOrderCommand(
                orderId,
                paymentId,
                "payment-service"));
    }

    private void handlePaymentFailed(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        log.warn("Payment failed for order: {}", orderId);
        // TODO: Handle payment failure - possibly cancel order
    }

    private void handlePaymentRefunded(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        log.info("Payment refunded for order: {}", orderId);
        // TODO: Handle refund - update order status
    }
}
