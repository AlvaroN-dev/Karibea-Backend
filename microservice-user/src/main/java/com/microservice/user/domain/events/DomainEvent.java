package com.microservice.user.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interfaz base para eventos de dominio
 */
public interface DomainEvent {
    
    UUID getEventId();
    
    String getEventType();
    
    LocalDateTime getOccurredAt();
    
    UUID getAggregateId();
}
