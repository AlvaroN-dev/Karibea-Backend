package com.microservice.inventory.kafka.consumer;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.microservice.inventory.domain.port.in.ConfirmReservationUseCase;
import com.microservice.inventory.domain.port.in.ReleaseReservationUseCase;

/**
 * Kafka consumer for order-related events.
 * Handles order confirmation and cancellation events from microservice-order.
 */
@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final ConfirmReservationUseCase confirmReservationUseCase;
    private final ReleaseReservationUseCase releaseReservationUseCase;

    public OrderEventConsumer(ConfirmReservationUseCase confirmReservationUseCase,
                              ReleaseReservationUseCase releaseReservationUseCase) {
        this.confirmReservationUseCase = confirmReservationUseCase;
        this.releaseReservationUseCase = releaseReservationUseCase;
    }

    /**
     * Handles order confirmed events.
     * When an order is confirmed (payment successful), confirm the inventory reservation.
     */
    @KafkaListener(topics = "order.confirmed", groupId = "inventory-service")
    public void handleOrderConfirmed(Map<String, Object> event) {
        log.info("Received order confirmed event: {}", event);
        
        try {
            UUID reservationId = extractUUID(event, "reservationId");
            UUID orderId = extractUUID(event, "orderId");
            UUID performedById = extractUUID(event, "userId");
            
            if (reservationId == null || orderId == null) {
                log.warn("Invalid order confirmed event - missing reservationId or orderId: {}", event);
                return;
            }
            
            confirmReservationUseCase.execute(
                new ConfirmReservationUseCase.ConfirmReservationCommand(
                    reservationId, 
                    orderId, 
                    performedById != null ? performedById : UUID.randomUUID()
                )
            );
            
            log.info("Successfully confirmed reservation {} for order {}", reservationId, orderId);
        } catch (Exception e) {
            log.error("Error processing order confirmed event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles order cancelled events.
     * When an order is cancelled, release the inventory reservation.
     */
    @KafkaListener(topics = "order.cancelled", groupId = "inventory-service")
    public void handleOrderCancelled(Map<String, Object> event) {
        log.info("Received order cancelled event: {}", event);
        
        try {
            UUID reservationId = extractUUID(event, "reservationId");
            String reason = event.get("reason") != null ? event.get("reason").toString() : "Order cancelled";
            
            if (reservationId == null) {
                log.warn("Invalid order cancelled event - missing reservationId: {}", event);
                return;
            }
            
            releaseReservationUseCase.execute(
                new ReleaseReservationUseCase.ReleaseReservationCommand(reservationId, reason)
            );
            
            log.info("Successfully released reservation {} due to order cancellation", reservationId);
        } catch (Exception e) {
            log.error("Error processing order cancelled event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles cart expired events.
     * When a cart expires, release any associated inventory reservations.
     */
    @KafkaListener(topics = "cart.expired", groupId = "inventory-service")
    public void handleCartExpired(Map<String, Object> event) {
        log.info("Received cart expired event: {}", event);
        
        try {
            UUID reservationId = extractUUID(event, "reservationId");
            
            if (reservationId == null) {
                log.warn("Invalid cart expired event - missing reservationId: {}", event);
                return;
            }
            
            releaseReservationUseCase.execute(
                new ReleaseReservationUseCase.ReleaseReservationCommand(reservationId, "Cart expired")
            );
            
            log.info("Successfully released reservation {} due to cart expiration", reservationId);
        } catch (Exception e) {
            log.error("Error processing cart expired event: {}", e.getMessage(), e);
        }
    }

    private UUID extractUUID(Map<String, Object> event, String key) {
        Object value = event.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof UUID) {
            return (UUID) value;
        }
        try {
            return UUID.fromString(value.toString());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format for key {}: {}", key, value);
            return null;
        }
    }
}
