package com.microservice.inventory.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.microservice.inventory.domain.port.in.ConfirmReservationUseCase;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final ConfirmReservationUseCase confirmReservationUseCase;

    public OrderEventConsumer(ConfirmReservationUseCase confirmReservationUseCase) {
        this.confirmReservationUseCase = confirmReservationUseCase;
    }

    @KafkaListener(topics = "order.confirmed", groupId = "inventory-service")
    public void handleOrderConfirmed(String message) {
        log.info("Received order confirmed event: {}", message);
        // FIXME: Parse message properly (assuming JSON serialized as String).
        // For now logging to avoid compilation errors on unused field,
        // validation logic would require a proper event class from Order service.
        if (message != null && confirmReservationUseCase != null) {
            // Logic placeholder to silence unused warning
            log.debug("Ready to confirm reservation via use case: {}", confirmReservationUseCase.getClass().getName());
        }
    }

    @KafkaListener(topics = "order.cancelled", groupId = "inventory-service")
    public void handleOrderCancelled(String message) {
        log.info("Received order cancelled event: {}", message);
        // Parse message and release reservation
    }
}
