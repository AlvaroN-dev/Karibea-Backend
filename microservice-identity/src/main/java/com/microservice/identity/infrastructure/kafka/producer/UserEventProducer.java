package com.microservice.identity.infrastructure.kafka.producer;

import com.microservice.identity.domain.events.EmailVerificationEvent;
import com.microservice.identity.domain.events.UserCreatedEvent;
import com.microservice.identity.domain.events.UserDeletedEvent;
import com.microservice.identity.domain.events.UserUpdatedEvent;
import com.microservice.identity.domain.exceptions.EventPublishingException;
import com.microservice.identity.domain.models.Role;
import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Kafka adapter implementation of EventPublisherPort.
 * Publishes domain events to Kafka topics.
 *
 * Key features:
 * - Async publishing with CompletableFuture
 * - Automatic retry on failure
 * - Structured logging with event details
 * - Partition routing by userId for ordering
 * - Error handling with custom exceptions
 *
 * Only active when kafka.enabled=true.
 */
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class UserEventProducer implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(UserEventProducer.class);

    // Map field names
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_ENABLED = "enabled";
    private static final String FIELD_EMAIL_VERIFIED = "emailVerified";
    private static final String FIELD_ROLES = "roles";
    private static final String FIELD_UPDATED_AT = "updatedAt";

    // Event placeholders
    private static final String PLACEHOLDER_EMAIL = "placeholder@email.com";
    private static final long TOKEN_EXPIRATION_SECONDS = 3600L;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.user-created:user-created-events}")
    private String userCreatedTopic;

    @Value("${kafka.topics.user-updated:user-updated-events}")
    private String userUpdatedTopic;

    @Value("${kafka.topics.user-deleted:user-deleted-events}")
    private String userDeletedTopic;

    @Value("${kafka.topics.email-verification:email-verification-events}")
    private String emailVerificationTopic;

    public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publishes UserCreatedEvent when a new user is created.
     *
     * @param user User domain model
     */
    @Override
    public void publishUserCreated(User user) {
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                extractRoleNames(user.getRoles()),
                user.isEnabled());

        publishEvent(userCreatedTopic, user.getId().toString(), event);
    }

    /**
     * Publishes UserUpdatedEvent when user information is updated.
     *
     * @param user User domain model
     */
    @Override
    public void publishUserUpdated(User user) {
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put(FIELD_USERNAME, user.getUsername());
        updatedFields.put(FIELD_EMAIL, user.getEmail());
        updatedFields.put(FIELD_ENABLED, user.isEnabled());
        updatedFields.put(FIELD_EMAIL_VERIFIED, user.isEmailVerified());
        updatedFields.put(FIELD_ROLES, extractRoleNames(user.getRoles()));
        updatedFields.put(FIELD_UPDATED_AT, user.getUpdatedAt());

        UserUpdatedEvent event = new UserUpdatedEvent(
                user.getId().toString(),
                updatedFields);

        publishEvent(userUpdatedTopic, user.getId().toString(), event);
    }

    /**
     * Publishes UserDeletedEvent when a user is deleted.
     *
     * @param userId User ID
     */
    @Override
    public void publishUserDeleted(UUID userId) {
        UserDeletedEvent event = new UserDeletedEvent(
                userId.toString(),
                "User deleted by system");

        publishEvent(userDeletedTopic, userId.toString(), event);
    }

    /**
     * Publishes EmailVerificationEvent when email verification is required.
     *
     * @param userId User ID
     */
    @Override
    public void publishEmailVerified(UUID userId) {
        // This method name is misleading - it should be
        // publishEmailVerificationRequired
        // For now, we'll create a placeholder event
        // In a real implementation, you'd pass the User object or verification details
        if (log.isWarnEnabled()) {
            log.warn("publishEmailVerified called with userId: {} - This should be refactored to accept User object",
                    userId);
        }

        // Placeholder implementation - should be refactored
        EmailVerificationEvent event = new EmailVerificationEvent(
                userId.toString(),
                PLACEHOLDER_EMAIL, // Should come from User
                UUID.randomUUID().toString(), // Should come from User.verificationToken
                Instant.now().plusSeconds(TOKEN_EXPIRATION_SECONDS)
        );

        publishEvent(emailVerificationTopic, userId.toString(), event);
    }

    /**
     * Publishes EmailVerificationEvent with full user details.
     * This is the preferred method for email verification events.
     *
     * @param user User domain model
     */
    public void publishEmailVerificationRequired(User user) {
        if (user.getVerificationToken() == null) {
            log.error("Cannot publish email verification event - user has no verification token: {}", user.getId());
            return;
        }

        EmailVerificationEvent event = new EmailVerificationEvent(
                user.getId().toString(),
                user.getEmail(),
                user.getVerificationToken(),
                Instant.now().plusSeconds(TOKEN_EXPIRATION_SECONDS)
        );

        publishEvent(emailVerificationTopic, user.getId().toString(), event);
    }

    /**
     * Generic method to publish events to Kafka.
     * Uses userId as partition key for ordering.
     *
     * @param topic Kafka topic
     * @param key   Partition key (userId)
     * @param event Event object
     */
    private void publishEvent(String topic, String key, Object event) {
        if (log.isDebugEnabled()) {
            log.debug("Publishing event to topic: {}, key: {}, event: {}", topic, key,
                    event.getClass().getSimpleName());
        }

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                handlePublishError(topic, key, event, ex);
            } else {
                handlePublishSuccess(result, event);
            }
        });
    }

    /**
     * Handles successful event publishing.
     *
     * @param result Send result
     * @param event  Event object
     */
    private void handlePublishSuccess(SendResult<String, Object> result, Object event) {
        if (log.isInfoEnabled()) {
            var metadata = result.getRecordMetadata();
            log.info("Successfully published event: type={}, topic={}, partition={}, offset={}",
                    event.getClass().getSimpleName(),
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset());
        }
    }

    /**
     * Handles event publishing errors.
     * Logs the error and throws EventPublishingException.
     *
     * @param topic Kafka topic
     * @param key   Partition key
     * @param event Event object
     * @param ex    Exception
     */
    private void handlePublishError(String topic, String key, Object event, Throwable ex) {
        if (log.isErrorEnabled()) {
            String eventType = event.getClass().getSimpleName();
            log.error("Failed to publish event: type={}, topic={}, key={}",
                    eventType, topic, key, ex);
        }

        throw new EventPublishingException(
                "Failed to publish event to Kafka",
                key,
                event.getClass().getSimpleName(),
                topic,
                ex);
    }

    /**
     * Extracts role names from Role objects.
     *
     * @param roles Set of Role objects
     * @return Set of role names
     */
    private Set<String> extractRoleNames(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}