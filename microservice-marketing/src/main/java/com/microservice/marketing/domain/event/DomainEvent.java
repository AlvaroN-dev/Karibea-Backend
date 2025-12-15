package com.microservice.marketing.domain.event;

import java.time.LocalDateTime;

public abstract class DomainEvent {
    private final String eventType;
    private final LocalDateTime occurredAt;

    public DomainEvent(String eventType) {
        this.eventType = eventType;
        this.occurredAt = LocalDateTime.now();
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
