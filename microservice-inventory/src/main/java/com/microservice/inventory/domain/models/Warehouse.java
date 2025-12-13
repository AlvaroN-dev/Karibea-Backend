package com.microservice.inventory.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Warehouse entity - Aggregate Root.
 * Represents physical storage location.
 */
public class Warehouse {

    private UUID id;
    private UUID externalStoreId;
    private String name;
    private String code;
    private Address address;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Warehouse() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getExternalStoreId() {
        return externalStoreId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Address getAddress() {
        return address;
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

    // Business methods
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateInfo(String name, Address address) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (address != null) {
            this.address = address;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public static class Builder {
        private final Warehouse warehouse = new Warehouse();

        public Builder id(UUID id) {
            warehouse.id = id;
            return this;
        }

        public Builder externalStoreId(UUID id) {
            warehouse.externalStoreId = id;
            return this;
        }

        public Builder name(String name) {
            warehouse.name = name;
            return this;
        }

        public Builder code(String code) {
            warehouse.code = code;
            return this;
        }

        public Builder address(Address address) {
            warehouse.address = address;
            return this;
        }

        public Builder isActive(boolean active) {
            warehouse.isActive = active;
            return this;
        }

        public Builder createdAt(LocalDateTime time) {
            warehouse.createdAt = time;
            return this;
        }

        public Builder updatedAt(LocalDateTime time) {
            warehouse.updatedAt = time;
            return this;
        }

        public Warehouse build() {
            if (warehouse.id == null)
                warehouse.id = UUID.randomUUID();
            if (warehouse.createdAt == null)
                warehouse.createdAt = LocalDateTime.now();
            if (warehouse.updatedAt == null)
                warehouse.updatedAt = warehouse.createdAt;
            Objects.requireNonNull(warehouse.name, "Name is required");
            Objects.requireNonNull(warehouse.code, "Code is required");
            return warehouse;
        }
    }
}
