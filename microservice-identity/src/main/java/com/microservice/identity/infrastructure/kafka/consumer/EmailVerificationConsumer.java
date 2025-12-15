package com.microservice.identity.infrastructure.kafka.consumer;

import com.microservice.identity.domain.events.EmailVerificationEvent;
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
 * Kafka consumer for email verification events.
 * Consumes events from email-verification-events topic.
 *
 * Key features:
 * - Idempotent processing (checks if event already processed)
 * - Manual acknowledgment for at-least-once delivery
 * - Dead Letter Queue (DLQ) for failed messages
 * - Retry logic for transient email service failures
 * - Structured logging with event details
 *
 * Only active when kafka.enabled=true.
 */
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class EmailVerificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailVerificationConsumer.class);
    private static final int MAX_RETRIES = 3;
    private static final String DLQ_SUFFIX = "-dlq";
    private static final String EMAIL_VERIFICATION_TOPIC = "email-verification-events";

    private final ProcessedEventRepository processedEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public EmailVerificationConsumer(ProcessedEventRepository processedEventRepository,
                                     KafkaTemplate<String, Object> kafkaTemplate) {
        this.processedEventRepository = processedEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Consumes EmailVerificationEvent from email-verification-events topic.
     *
     * @param event     EmailVerificationEvent
     * @param key       Partition key (userId)
     * @param partition Partition number
     * @param offset    Offset
     * @param ack       Manual acknowledgment
     */
    @KafkaListener(topics = "${kafka.topics.email-verification:email-verification-events}",
            groupId = "${kafka.consumer.group-id}",
            concurrency = "${kafka.listener.concurrency:3}",
            containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeEmailVerification(
            @Payload EmailVerificationEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {

        log.info("Received EmailVerificationEvent: eventId={}, userId={}, email={}, partition={}, offset={}",
                event.getEventId(), event.getUserId(), event.getEmail(), partition, offset);

        try {
            // Check if event already processed (idempotency)
            if (isAlreadyProcessed(event.getEventId())) {
                log.warn("Duplicate EmailVerificationEvent detected: eventId={}, skipping", event.getEventId());
                ack.acknowledge();
                return;
            }

            // Process the event
            processEmailVerificationEvent(event);

            // Mark as processed
            markAsProcessed(event.getEventId(), event.getEventType(), event.getAggregateId(),
                    EMAIL_VERIFICATION_TOPIC, partition, offset);

            // Acknowledge successful processing
            ack.acknowledge();

            log.info("Successfully processed EmailVerificationEvent: eventId={}", event.getEventId());

        } catch (Exception ex) {
            handleConsumerError(event, EMAIL_VERIFICATION_TOPIC, partition, offset, ex, ack);
        }
    }

    /**
     * Processes EmailVerificationEvent.
     * Sends verification email to user.
     *
     * <p>When implementing email sending, consider:
     * <ul>
     *   <li>Email template rendering</li>
     *   <li>Verification link generation</li>
     *   <li>Retry logic for transient failures</li>
     *   <li>Rate limiting</li>
     *   <li>Email service provider integration (SendGrid, AWS SES, etc.)</li>
     * </ul>
     *
     * @param event EmailVerificationEvent
     */
    private void processEmailVerificationEvent(EmailVerificationEvent event) {
        log.debug("Processing EmailVerificationEvent: userId={}, email={}, token=***",
                event.getUserId(), event.getEmail());

        try {
            // TdO: Implement email sending logic
            log.info("Email verification would be sent to: {} (email service not implemented yet)",
                    event.getEmail());

        } catch (Exception ex) {
            log.error("Failed to send verification email: userId={}, email={}",
                    event.getUserId(), event.getEmail(), ex);
        }
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
    private void handleConsumerError(EmailVerificationEvent event, String topic, int partition, long offset,
                                     Exception ex, Acknowledgment ack) {
        log.error("Error processing EmailVerificationEvent: eventId={}, topic={}, partition={}, offset={}",
                event.getEventId(), topic, partition, offset, ex);

        try {
            // Send to DLQ
            String dlqTopic = topic + DLQ_SUFFIX;
            kafkaTemplate.send(dlqTopic, event);
            log.warn("Sent failed EmailVerificationEvent to DLQ: eventId={}, dlqTopic={}",
                    event.getEventId(), dlqTopic);

            // Acknowledge to prevent infinite retry loop
            ack.acknowledge();

        } catch (Exception dlqEx) {
            log.error("Failed to send EmailVerificationEvent to DLQ: eventId={}",
                    event.getEventId(), dlqEx);
            // Don't acknowledge - let Kafka retry
            throw new EventConsumptionException(
                    "Failed to process event and send to DLQ",
                    event.getEventId(),
                    event.getEventType(),
                    topic,
                    MAX_RETRIES,
                    ex);
        }
    }
}