package com.microservice.shipping.domain.port.in;

import com.microservice.shipping.domain.models.TrackingEventStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Inbound port for adding tracking events.
 */
public interface AddTrackingEventPort {

    void execute(AddTrackingEventCommand command);

    record AddTrackingEventCommand(
            UUID shipmentId,
            TrackingEventStatusEnum status,
            String location,
            String description,
            LocalDateTime occurredAt) {
    }
}
