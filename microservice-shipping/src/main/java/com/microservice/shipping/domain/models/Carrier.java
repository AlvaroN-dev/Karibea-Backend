package com.microservice.shipping.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Carrier entity - represents a shipping carrier.
 * PURE DOMAIN - No framework dependencies.
 */
public class Carrier {

    private UUID id;
    private String code;
    private String name;
    private String trackingUrlTemplate;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Carrier() {
        this.id = UUID.randomUUID();
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Carrier create(String code, String name, String trackingUrlTemplate) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Carrier code cannot be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Carrier name cannot be blank");
        }

        Carrier carrier = new Carrier();
        carrier.code = code.toUpperCase();
        carrier.name = name;
        carrier.trackingUrlTemplate = trackingUrlTemplate;

        return carrier;
    }

    public String buildTrackingUrl(String trackingNumber) {
        if (trackingUrlTemplate == null || trackingNumber == null) {
            return null;
        }
        return trackingUrlTemplate.replace("{trackingNumber}", trackingNumber);
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTrackingUrlTemplate() {
        return trackingUrlTemplate;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Builder for reconstitution
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Carrier carrier = new Carrier();

        public Builder id(UUID id) {
            carrier.id = id;
            return this;
        }

        public Builder code(String code) {
            carrier.code = code;
            return this;
        }

        public Builder name(String name) {
            carrier.name = name;
            return this;
        }

        public Builder trackingUrlTemplate(String url) {
            carrier.trackingUrlTemplate = url;
            return this;
        }

        public Builder isActive(boolean active) {
            carrier.isActive = active;
            return this;
        }

        public Builder createdAt(LocalDateTime dt) {
            carrier.createdAt = dt;
            return this;
        }

        public Builder updatedAt(LocalDateTime dt) {
            carrier.updatedAt = dt;
            return this;
        }

        public Carrier build() {
            return carrier;
        }
    }
}
