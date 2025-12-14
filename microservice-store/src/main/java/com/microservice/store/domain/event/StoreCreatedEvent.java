package com.microservice.store.domain.event;

import java.time.OffsetDateTime;

public record StoreCreatedEvent(
        Long id,
        String externalOwnerUserId,
        String name,
        String email,
        OffsetDateTime createdAt) {
}
