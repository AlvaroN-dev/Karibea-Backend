package com.microservice.user.domain.events;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Evento de dominio - Perfil de usuario actualizado
 */
public record UserProfileUpdatedEvent(
    UUID eventId,
    UUID aggregateId,
    UUID externalUserId,
    Map<String, Object> changes,
    LocalDateTime occurredAt
) implements DomainEvent {
    
    public static UserProfileUpdatedEvent create(UUID profileId, UUID externalUserId, 
                                                  Map<String, Object> changes) {
        return new UserProfileUpdatedEvent(
            UUID.randomUUID(),
            profileId,
            externalUserId,
            changes,
            LocalDateTime.now()
        );
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "profile.updated";
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
