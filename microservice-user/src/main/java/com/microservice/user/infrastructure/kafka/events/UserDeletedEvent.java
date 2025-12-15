package com.microservice.user.infrastructure.kafka.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento recibido cuando se elimina un usuario en Identity
 */
public record UserDeletedEvent(
    UUID eventId,
    UUID userId,
    LocalDateTime occurredAt
) {}
