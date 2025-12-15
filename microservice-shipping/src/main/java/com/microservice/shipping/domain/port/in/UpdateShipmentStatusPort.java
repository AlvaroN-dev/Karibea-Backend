package com.microservice.shipping.domain.port.in;

import com.microservice.shipping.domain.models.ShipmentStatusEnum;

import java.util.UUID;

/**
 * Inbound port for updating shipment status.
 */
public interface UpdateShipmentStatusPort {

    void execute(UpdateStatusCommand command);

    record UpdateStatusCommand(
            UUID shipmentId,
            ShipmentStatusEnum newStatus,
            String location,
            String reason,
            String updatedBy) {
    }
}
