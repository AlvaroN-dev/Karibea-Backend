package com.microservice.user.infrastructure.kafka.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento recibido cuando se crea un usuario en Identity
 */
public record UserCreatedEvent(
    UUID eventId,
    UUID userId,
    String username,
    String email,
    LocalDateTime occurredAt
) {}
