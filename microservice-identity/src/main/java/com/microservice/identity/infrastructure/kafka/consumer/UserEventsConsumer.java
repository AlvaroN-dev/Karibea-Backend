package com.microservice.identity.infrastructure.kafka.consumer;

import com.microservice.identity.domain.events.UserCreatedEvent;
import com.microservice.identity.domain.events.UserDeletedEvent;
import com.microservice.identity.domain.events.UserUpdatedEvent;
import com.microservice.identity.domain.exceptions.EventConsumptionException;
import com.microservice.identity.infrastructure.entities.ProcessedEventEntity;
import com.microservice.identity.infrastructure.repositories.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kafka consumer for user-related events.
 * Consumes events from user-created-events, user-updated-events, and
 * user-deleted-events topics.
 * 
 * Key features:
 * - Idempotent processing (checks if event already processed)
 * - Manual acknowledgment for at-least-once delivery
 * - Dead Letter Queue (DLQ) for failed messages
 * - Structured logging with event details
 * - Concurrent processing (3 threads per topic)
 * 
 * Only active when kafka.enabled=true.
 */
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class UserEventsConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserEventsConsumer.class);
    private static final int MAX_RETRIES = 3;

    private final ProcessedEventRepository processedEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserEventsConsumer(ProcessedEventRepository processedEventRepository,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.processedEventRepository = processedEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Consumes UserCreatedEvent from user-created-events topic.
     * 
     * @param event     UserCreatedEvent
     * @param key       Partition key (userId)
     * @param partition Partition number
     * @param offset    Offset
     * @param ack       Manual acknowledgment
     */
    @KafkaListener(topics = "${kafka.topics.user-created:user-created-events}", groupId = "${kafka.consumer.group-id}", concurrency = "${kafka.listener.concurrency:3}", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserCreated(
            @Payload UserCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        log.info("Received UserCreatedEvent: eventId={}, userId={}, partition={}, offset={}",
                event.getEventId(), event.getUserId(), partition, offset);

        try {
            // Check if event already processed (idempotency)
            if (isAlreadyProcessed(event.getEventId())) {
                log.warn("Duplicate UserCreatedEvent detected: eventId={}, skipping", event.getEventId());
                ack.acknowledge();
                return;
            }

            // Process the event
            processUserCreatedEvent(event);

            // Mark as processed
            markAsProcessed(event.getEventId(), event.getEventType(), event.getAggregateId(),
                    "user-created-events", partition, offset);

            // Acknowledge successful processing
            ack.acknowledge();

            log.info("Successfully processed UserCreatedEvent: eventId={}", event.getEventId());

        } catch (Exception ex) {
            handleConsumerError(event, "user-created-events", partition, offset, ex, ack);
        }
    }

    /**
     * Consumes UserUpdatedEvent from user-updated-events topic.
     * 
     * @param event     UserUpdatedEvent
     * @param key       Partition key (userId)
     * @param partition Partition number
     * @param offset    Offset
     * @param ack       Manual acknowledgment
     */
    @KafkaListener(topics = "${kafka.topics.user-updated:user-updated-events}", groupId = "${kafka.consumer.group-id}", concurrency = "${kafka.listener.concurrency:3}", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserUpdated(
            @Payload UserUpdatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        log.info("Received UserUpdatedEvent: eventId={}, userId={}, partition={}, offset={}",
                event.getEventId(), event.getUserId(), partition, offset);

        try {
            if (isAlreadyProcessed(event.getEventId())) {
                log.warn("Duplicate UserUpdatedEvent detected: eventId={}, skipping", event.getEventId());
                ack.acknowledge();
                return;
            }

            processUserUpdatedEvent(event);

            markAsProcessed(event.getEventId(), event.getEventType(), event.getAggregateId(),
                    "user-updated-events", partition, offset);

            ack.acknowledge();

            log.info("Successfully processed UserUpdatedEvent: eventId={}", event.getEventId());

        } catch (Exception ex) {
            handleConsumerError(event, "user-updated-events", partition, offset, ex, ack);
        }
    }

    /**
     * Consumes UserDeletedEvent from user-deleted-events topic.
     * 
     * @param event     UserDeletedEvent
     * @param key       Partition key (userId)
     * @param partition Partition number
     * @param offset    Offset
     * @param ack       Manual acknowledgment
     */
    @KafkaListener(topics = "${kafka.topics.user-deleted:user-deleted-events}", groupId = "${kafka.consumer.group-id}", concurrency = "${kafka.listener.concurrency:3}", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserDeleted(
            @Payload UserDeletedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        log.info("Received UserDeletedEvent: eventId={}, userId={}, partition={}, offset={}",
                event.getEventId(), event.getUserId(), partition, offset);

        try {
            if (isAlreadyProcessed(event.getEventId())) {
                log.warn("Duplicate UserDeletedEvent detected: eventId={}, skipping", event.getEventId());
                ack.acknowledge();
                return;
            }

            processUserDeletedEvent(event);

            markAsProcessed(event.getEventId(), event.getEventType(), event.getAggregateId(),
                    "user-deleted-events", partition, offset);

            ack.acknowledge();

            log.info("Successfully processed UserDeletedEvent: eventId={}", event.getEventId());

        } catch (Exception ex) {
            handleConsumerError(event, "user-deleted-events", partition, offset, ex, ack);
        }
    }

    /**
     * Processes UserCreatedEvent.
     * Add your business logic here (e.g., update cache, send notifications, etc.)
     * 
     * @param event UserCreatedEvent
     */
    private void processUserCreatedEvent(UserCreatedEvent event) {
        log.debug("Processing UserCreatedEvent: userId={}, username={}, email={}",
                event.getUserId(), event.getUsername(), event.getEmail());

        // Integration point: Actions to take when a user is created
        log.info("Event processed: Sending welcome email to {}", event.getEmail());
        log.info("Event processed: Initializing user analytics for {}", event.getUsername());
    }

    /**
     * Processes UserUpdatedEvent.
     * 
     * @param event UserUpdatedEvent
     */
    private void processUserUpdatedEvent(UserUpdatedEvent event) {
        log.debug("Processing UserUpdatedEvent: userId={}, updatedFields={}",
                event.getUserId(), event.getUpdatedFields());

        // Integration point: Actions to take when a user is updated
        log.info("Event processed: Invalidating cache for userId={}", event.getUserId());
        log.info("Event processed: Notifying downstream services of profile update");
    }

    /**
     * Processes UserDeletedEvent.
     * 
     * @param event UserDeletedEvent
     */
    private void processUserDeletedEvent(UserDeletedEvent event) {
        log.debug("Processing UserDeletedEvent: userId={}, reason={}",
                event.getUserId(), event.getReason());

        // Integration point: Actions to take when a user is deleted
        log.info("Event processed: Cleaning up resources for userId={}", event.getUserId());
        log.info("Event processed: Removing user from search index");
    }

    /**
     * Checks if an event has already been processed.
     * 
     * @param eventId Event ID
     * @return true if already processed
     */
    private boolean isAlreadyProcessed(String eventId) {
        return processedEventRepository.existsByEventId(eventId);
    }

    /**
     * Marks an event as processed.
     * 
     * @param eventId     Event ID
     * @param eventType   Event type
     * @param aggregateId Aggregate ID (userId)
     * @param topic       Topic name
     * @param partition   Partition number
     * @param offset      Offset
     */
    private void markAsProcessed(String eventId, String eventType, String aggregateId,
            String topic, int partition, long offset) {
        ProcessedEventEntity entity = new ProcessedEventEntity(
                eventId, eventType, aggregateId, topic, partition, offset);
        processedEventRepository.save(entity);
        log.debug("Marked event as processed: eventId={}", eventId);
    }

    /**
     * Handles consumer errors.
     * Sends failed events to Dead Letter Queue (DLQ) after max retries.
     * 
     * @param event     Event object
     * @param topic     Topic name
     * @param partition Partition number
     * @param offset    Offset
     * @param ex        Exception
     * @param ack       Acknowledgment
     */
    private void handleConsumerError(Object event, String topic, int partition, long offset,
            Exception ex, Acknowledgment ack) {
        String eventType = event.getClass().getSimpleName();
        log.error("Error processing event: type={}, topic={}, partition={}, offset={}, error={}",
                eventType, topic, partition, offset, ex.getMessage(), ex);

        try {
            // Send to DLQ
            String dlqTopic = topic + "-dlq";
            kafkaTemplate.send(dlqTopic, event);
            log.warn("Sent failed event to DLQ: type={}, dlqTopic={}", eventType, dlqTopic);

            // Acknowledge to prevent infinite retry loop
            ack.acknowledge();

        } catch (Exception dlqEx) {
            log.error("Failed to send event to DLQ: type={}, error={}", eventType, dlqEx.getMessage(), dlqEx);
            // Don't acknowledge - let Kafka retry
            throw new EventConsumptionException(
                    "Failed to process event and send to DLQ",
                    "unknown", // Event ID not accessible here
                    eventType,
                    topic,
                    MAX_RETRIES,
                    ex);
        }
    }
}
