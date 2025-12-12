package com.microservice.user.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de dominio - Perfil de usuario creado
 */
public record UserProfileCreatedEvent(
    UUID eventId,
    UUID aggregateId,
    UUID externalUserId,
    String firstName,
    String lastName,
    String fullName,
    LocalDateTime occurredAt
) implements DomainEvent {
    
    public static UserProfileCreatedEvent create(UUID profileId, UUID externalUserId, 
                                                  String firstName, String lastName) {
        return new UserProfileCreatedEvent(
            UUID.randomUUID(),
            profileId,
            externalUserId,
            firstName,
            lastName,
            firstName + " " + lastName,
            LocalDateTime.now()
        );
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public String getEventType() {
        return "profile.created";
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
