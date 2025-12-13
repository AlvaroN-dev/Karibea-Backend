package com.microservice.notification.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID getEventId();

    String getEventType();

    LocalDateTime getOccurredAt();

    UUID getAggregateId();
}
