package com.microservice.order.domain.port.in;

import com.microservice.order.domain.models.OrderStatusEnum;

import java.util.UUID;

/**
 * Inbound port for changing order status.
 */
public interface ChangeOrderStatusPort {

    void execute(ChangeOrderStatusCommand command);

    record ChangeOrderStatusCommand(
            UUID orderId,
            OrderStatusEnum newStatus,
            String reason,
            String changedBy,
            UUID relatedEntityId // paymentId, shipmentId, etc.
    ) {
    }
}
