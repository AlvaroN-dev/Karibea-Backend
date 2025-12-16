package com.microservice.user.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de dominio - Perfil de usuario eliminado
 */
public record UserProfileDeletedEvent(
    UUID eventId,
    UUID aggregateId,
    UUID externalUserId,
    LocalDateTime occurredAt
) implements DomainEvent {
    
    public static UserProfileDeletedEvent create(UUID profileId, UUID externalUserId) {
        return new UserProfileDeletedEvent(
            UUID.randomUUID(),
            profileId,
            externalUserId,
            LocalDateTime.now()
        );
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "profile.deleted";
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
