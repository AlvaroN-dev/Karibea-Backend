package com.microservice.identity.domain.port.out;

import com.microservice.identity.domain.models.User;

import java.util.UUID;

public interface EventPublisherPort {
    void publishUserCreated(User user);
    void publishUserUpdated(User user);
    void publishUserDeleted(UUID userId);
    void publishEmailVerified(UUID userId);
}
