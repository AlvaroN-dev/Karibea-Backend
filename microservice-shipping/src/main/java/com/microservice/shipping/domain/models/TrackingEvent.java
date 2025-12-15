package com.microservice.shipping.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TrackingEvent entity - represents a tracking update for a shipment.
 * PURE DOMAIN - No framework dependencies.
 */
public class TrackingEvent {

    private UUID id;
    private UUID shipmentId;
    private TrackingEventStatusEnum status;
    private String location;
    private String description;
    private LocalDateTime occurredAt;
    private LocalDateTime createdAt;

    private TrackingEvent() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public static TrackingEvent create(
            TrackingEventStatusEnum status,
            String location,
            String description,
            LocalDateTime occurredAt) {

        TrackingEvent event = new TrackingEvent();
        event.status = status;
        event.location = location;
        event.description = description;
        event.occurredAt = occurredAt != null ? occurredAt : LocalDateTime.now();

        return event;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getShipmentId() {
        return shipmentId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Builder for reconstitution
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TrackingEvent event = new TrackingEvent();

        public Builder id(UUID id) {
            event.id = id;
            return this;
        }

        public Builder shipmentId(UUID id) {
            event.shipmentId = id;
            return this;
        }

        public Builder status(TrackingEventStatusEnum status) {
            event.status = status;
            return this;
        }

        public Builder location(String loc) {
            event.location = loc;
            return this;
        }

        public Builder description(String desc) {
            event.description = desc;
            return this;
        }

        public Builder occurredAt(LocalDateTime dt) {
            event.occurredAt = dt;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            event.createdAt = dt;
            return this;
        }

        public TrackingEvent build() {
            return event;
        }
    }
}
