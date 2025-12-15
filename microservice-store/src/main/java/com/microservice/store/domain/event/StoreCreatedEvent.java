package com.microservice.store.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;

public record StoreCreatedEvent(
        UUID id,
        UUID externalOwnerUserId,
        String name,
        String email,
        OffsetDateTime createdAt) {
}
        