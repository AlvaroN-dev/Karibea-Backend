package com.microservice.search.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento que representa un producto recibido desde el microservicio de catálogo
 * para ser indexado en el motor de búsqueda.
 */
public record ProductReceivedEvent(
        UUID eventId,
        UUID productId,
        UUID storeId,
        String name,
        String description,
        String brand,
        java.math.BigDecimal price,
        String category,
        String imageUrl,
        boolean isActive,
        Instant occurredAt) implements DomainEvent {

    public ProductReceivedEvent {
        if (eventId == null)
            eventId = UUID.randomUUID();
        if (occurredAt == null)
            occurredAt = Instant.now();
    }

    public static ProductReceivedEvent create(
            UUID productId,
            UUID storeId,
            String name,
            String description,
            String brand,
            java.math.BigDecimal price,
            String category,
            String imageUrl,
            boolean isActive) {
        return new ProductReceivedEvent(
                UUID.randomUUID(),
                productId,
                storeId,
                name,
                description,
                brand,
                price,
                category,
                imageUrl,
                isActive,
                Instant.now());
    }

    @Override
    public String eventType() {
        return "ProductReceived";
    }
}
