package com.microservice.order.domain.port.in;

import java.util.UUID;

/**
 * Inbound port for cancelling orders.
 */
public interface CancelOrderPort {

    void execute(CancelOrderCommand command);

    record CancelOrderCommand(
            UUID orderId,
            String reason,
            String cancelledBy) {
    }
}
