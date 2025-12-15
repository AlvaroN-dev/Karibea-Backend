package com.microservice.shipping.domain.events;

import com.microservice.shipping.domain.models.TrackingEvent;
import com.microservice.shipping.domain.models.TrackingEventStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when a tracking event is added.
 */
public class TrackingEventAddedEvent extends DomainEvent {

    private final UUID trackingEventId;
    private final TrackingEventStatusEnum status;
    private final String location;
    private final String description;
    private final LocalDateTime occurredAt;

    private TrackingEventAddedEvent(UUID shipmentId, TrackingEvent trackingEvent) {
        super(shipmentId, "TrackingEventAdded");
        this.trackingEventId = trackingEvent.getId();
        this.status = trackingEvent.getStatus();
        this.location = trackingEvent.getLocation();
        this.description = trackingEvent.getDescription();
        this.occurredAt = trackingEvent.getOccurredAt();
    }

    public static TrackingEventAddedEvent of(UUID shipmentId, TrackingEvent trackingEvent) {
        return new TrackingEventAddedEvent(shipmentId, trackingEvent);
    }

    public UUID getTrackingEventId() {
        return trackingEventId;
    }

    public TrackingEventStatusEnum getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
