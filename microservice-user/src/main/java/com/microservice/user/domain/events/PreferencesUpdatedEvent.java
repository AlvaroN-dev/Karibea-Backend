package com.microservice.user.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de dominio - Preferencias actualizadas
 */
public record PreferencesUpdatedEvent(
    UUID eventId,
    UUID aggregateId,
    UUID externalUserId,
    UUID languageId,
    UUID currencyId,
    boolean notificationEmail,
    boolean notificationPush,
    LocalDateTime occurredAt
) implements DomainEvent {
    
    public static PreferencesUpdatedEvent create(UUID preferencesId, UUID externalUserId,
                                                  UUID languageId, UUID currencyId,
                                                  boolean notificationEmail, boolean notificationPush) {
        return new PreferencesUpdatedEvent(
            UUID.randomUUID(),
            preferencesId,
            externalUserId,
            languageId,
            currencyId,
            notificationEmail,
            notificationPush,
            LocalDateTime.now()
        );
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "preferences.updated";
    }
    
    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
    
    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }
}
