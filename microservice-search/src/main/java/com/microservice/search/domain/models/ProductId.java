package com.microservice.search.domain.models;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa el identificador único de un producto.
 * Inmutable y auto-validante.
 */
public record ProductId(UUID value) {

    public ProductId {
        Objects.requireNonNull(value, "Product ID value cannot be null");
    }

    public static ProductId fromString(String id) {
        return new ProductId(UUID.fromString(id));
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
