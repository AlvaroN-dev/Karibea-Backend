package com.microservice.identity.infrastructure.adapters;

import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Event Publisher Adapter for publishing domain events.
 * Implements EventPublisherPort with logging (can be replaced with actual event
 * publisher).
 * Follows hexagonal architecture by adapting event infrastructure to domain.
 *
 * TDO: Replace with actual event publisher implementation (e.g., Kafka,
 * RabbitMQ, Spring Events)
 */
@Component
public class EventPublisherAdapter implements EventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisherAdapter.class);

    // TDO: Implement actual event publishing (e.g., Kafka, RabbitMQ)
    @Override
    public void publishUserCreated(User user) {
        logger.info("Publishing UserCreated event for user: {} (ID: {})", user.getUsername(), user.getId());
        logger.debug("Event payload: userId={}, username={}, email={}",
                user.getId(), user.getUsername(), user.getEmail());
    }

    // TDO: Implement actual event publishing
    @Override
    public void publishUserUpdated(User user) {
        logger.info("Publishing UserUpdated event for user: {} (ID: {})", user.getUsername(), user.getId());
        logger.debug("Event payload: userId={}, username={}, email={}",
                user.getId(), user.getUsername(), user.getEmail());
    }

    // TDO: Implement actual event publishing
    @Override
    public void publishUserDeleted(UUID userId) {
        logger.info("Publishing UserDeleted event for user ID: {}", userId);
        logger.debug("Event payload: userId={}", userId);
    }


    // TDO: Implement actual event publishing
    @Override
    public void publishEmailVerified(UUID userId) {
        logger.info("Publishing EmailVerified event for user ID: {}", userId);
        logger.debug("Event payload: userId={}", userId);
    }
}
