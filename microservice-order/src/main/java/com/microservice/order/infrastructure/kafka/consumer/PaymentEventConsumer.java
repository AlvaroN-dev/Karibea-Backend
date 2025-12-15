package com.microservice.order.infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.domain.port.in.CancelOrderPort;
import com.microservice.order.domain.port.in.ChangeOrderStatusPort;
import com.microservice.order.domain.port.in.ConfirmOrderPort;
import com.microservice.order.domain.models.enums.OrderStatusEnum;
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
 * 
 * Handles payment-related events from the payment microservice:
 * - PaymentCompleted: Confirms the order
 * - PaymentFailed: Cancels the order
 * - PaymentRefunded: Updates order status to refunded
 * 
 * This consumer follows event-driven architecture principles,
 * ensuring loose coupling between order and payment services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final ConfirmOrderPort confirmOrderPort;
    private final CancelOrderPort cancelOrderPort;
    private final ChangeOrderStatusPort changeOrderStatusPort;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "payment-events",
            groupId = "${spring.kafka.consumer.group-id:order-service}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handlePaymentEvent(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            Acknowledgment ack) {

        try {
            log.info("Received payment event with key: {}", key);
            log.debug("Payment event payload: {}", payload);

            JsonNode event = objectMapper.readTree(payload);
            String eventType = event.get("eventType").asText();
            String eventId = event.has("eventId") ? event.get("eventId").asText() : "unknown";

            log.info("Processing payment event: {} (eventId: {})", eventType, eventId);

            switch (eventType) {
                case "PaymentCompleted" -> handlePaymentCompleted(event);
                case "PaymentFailed" -> handlePaymentFailed(event);
                case "PaymentRefunded" -> handlePaymentRefunded(event);
                default -> log.warn("Unknown payment event type: {}. Ignoring.", eventType);
            }

            ack.acknowledge();
            log.info("Payment event {} processed successfully", eventType);

        } catch (Exception ex) {
            log.error("Error processing payment event: {}", ex.getMessage(), ex);
            // Don't acknowledge - message will be retried based on error handler configuration
        }
    }

    /**
     * Handle payment completed - confirms the order.
     */
    private void handlePaymentCompleted(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        UUID paymentId = UUID.fromString(event.get("paymentId").asText());

        log.info("Processing PaymentCompleted for order: {} with payment: {}", orderId, paymentId);

        confirmOrderPort.execute(new ConfirmOrderPort.ConfirmOrderCommand(
                orderId,
                paymentId,
                "payment-service"
        ));

        log.info("Order {} confirmed after payment {}", orderId, paymentId);
    }

    /**
     * Handle payment failed - cancels the order.
     */
    private void handlePaymentFailed(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        String failureReason = event.has("reason") ? event.get("reason").asText() : "Payment failed";

        log.warn("Processing PaymentFailed for order: {} - Reason: {}", orderId, failureReason);

        cancelOrderPort.execute(new CancelOrderPort.CancelOrderCommand(
                orderId,
                "Payment failed: " + failureReason,
                "payment-service"
        ));

        log.info("Order {} cancelled due to payment failure", orderId);
    }

    /**
     * Handle payment refunded - updates order status to refunded.
     */
    private void handlePaymentRefunded(JsonNode event) {
        UUID orderId = UUID.fromString(event.get("orderId").asText());
        String refundReason = event.has("reason") ? event.get("reason").asText() : "Payment refunded";

        log.info("Processing PaymentRefunded for order: {} - Reason: {}", orderId, refundReason);

        changeOrderStatusPort.execute(new ChangeOrderStatusPort.ChangeOrderStatusCommand(
                orderId,
                OrderStatusEnum.REFUNDED,
                refundReason,
                "payment-service",
                null
        ));

        log.info("Order {} marked as refunded", orderId);
    }
}
