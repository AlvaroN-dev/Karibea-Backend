package com.microservice.shipping.domain.port.in;

import java.util.UUID;

/**
 * Inbound port for cancelling shipments.
 */
public interface CancelShipmentPort {

    void execute(CancelShipmentCommand command);

    record CancelShipmentCommand(
            UUID shipmentId,
            String reason,
            String cancelledBy) {
    }
}
