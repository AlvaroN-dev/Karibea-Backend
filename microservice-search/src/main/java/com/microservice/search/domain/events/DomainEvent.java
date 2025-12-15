package com.microservice.search.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Interfaz base para eventos de dominio.
 */
public interface DomainEvent {
    
    UUID eventId();
    
    Instant occurredAt();
    
    String eventType();
}
