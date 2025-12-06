package com.microservice.shoppingCart.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Estado del carrito (ej: OPEN, CHECKOUT, ABANDONED).
 */
public class Status {

    private UUID id;
    private String name;
    private String description;
    private Instant createdAt;

    public Status() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public Status(UUID id, String name, String description) {
        this();
        this.id = id == null ? UUID.randomUUID() : id;
        this.name = name;
        this.description = description;
    }

    // Getters / Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        return Objects.equals(id, status.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
