package com.microservice.payment.infrastructure.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.payment.domain.events.DomainEvent;
import com.microservice.payment.domain.events.RefundProcessedEvent;
import com.microservice.payment.domain.events.TransactionCompletedEvent;
import com.microservice.payment.domain.events.TransactionCreatedEvent;
import com.microservice.payment.domain.events.TransactionFailedEvent;
import com.microservice.payment.domain.port.out.EventPublisherPort;
import com.microservice.payment.infrastructure.kafka.config.KafkaTopics;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka Producer for Payment domain events.
 * 
 * <p>
 * Implements EventPublisherPort to publish domain events to Kafka topics.
 * This producer handles serialization and async publishing with proper
 * error handling and logging.
 * </p>
 * 
 * <p>
 * <b>Event Topics:</b>
 * </p>
 * <ul>
 * <li>{@code payment.transaction.created} - New transaction created</li>
 * <li>{@code payment.transaction.completed} - Transaction successfully completed</li>
 * <li>{@code payment.transaction.failed} - Transaction processing failed</li>
 * <li>{@code payment.refund.processed} - Refund processed</li>
 * </ul>
 * 
 * <p>
 * <b>Why Event-Driven over REST:</b>
 * </p>
 * <ul>
 * <li>Loose coupling between services</li>
 * <li>Temporal decoupling - consumers don't need to be online</li>
 * <li>Better resilience and scalability</li>
 * <li>Enables event sourcing patterns</li>
 * <li>Natural audit trail</li>
 * </ul>
 */
@Component
public class PaymentEventProducer implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Publishes a domain event to the appropriate Kafka topic.
     * The topic is automatically resolved based on the event type.
     *
     * @param event the domain event to publish
     */
    @Override
    public void publish(DomainEvent event) {
        String topic = resolveTopicForEvent(event);
        publish(event, topic);
    }

    /**
     * Publishes a domain event to a specific Kafka topic.
     *
     * @param event the domain event to publish
     * @param topic the target Kafka topic
     */
    @Override
    public void publish(DomainEvent event, String topic) {
        try {
            String payload = serializeEvent(event);
            String key = event.getAggregateId().toString();

            sendMessage(topic, key, payload, event.getEventType());

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {} - {}", event.getEventType(), e.getMessage());
            throw new RuntimeException("Failed to serialize event for Kafka", e);
        }
    }

    /**
     * Publishes a transaction created event.
     *
     * @param event the transaction created event
     */
    public void publishTransactionCreated(TransactionCreatedEvent event) {
        publish(event, KafkaTopics.TRANSACTION_CREATED);
    }

    /**
     * Publishes a transaction completed event.
     *
     * @param event the transaction completed event
     */
    public void publishTransactionCompleted(TransactionCompletedEvent event) {
        publish(event, KafkaTopics.TRANSACTION_COMPLETED);
    }

    /**
     * Publishes a transaction failed event.
     *
     * @param event the transaction failed event
     */
    public void publishTransactionFailed(TransactionFailedEvent event) {
        publish(event, KafkaTopics.TRANSACTION_FAILED);
    }

    /**
     * Publishes a refund processed event.
     *
     * @param event the refund processed event
     */
    public void publishRefundProcessed(RefundProcessedEvent event) {
        publish(event, KafkaTopics.REFUND_PROCESSED);
    }

    /**
     * Serializes the domain event to JSON string.
     */
    private String serializeEvent(DomainEvent event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    /**
     * Sends a message to Kafka with async handling.
     */
    private void sendMessage(String topic, String key, String payload, String eventType) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, payload);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                handleSendFailure(topic, eventType, ex);
            } else {
                handleSendSuccess(topic, eventType, result);
            }
        });
    }

    /**
     * Handles successful message send.
     */
    private void handleSendSuccess(String topic, String eventType, SendResult<String, String> result) {
        log.info("Published event [{}] to topic [{}] | partition: {} | offset: {}",
                eventType,
                topic,
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
    }

    /**
     * Handles message send failure.
     */
    private void handleSendFailure(String topic, String eventType, Throwable ex) {
        log.error("Failed to publish event [{}] to topic [{}]: {}",
                eventType, topic, ex.getMessage(), ex);
    }

    /**
     * Resolves the appropriate Kafka topic based on the event type.
     */
    private String resolveTopicForEvent(DomainEvent event) {
        if (event instanceof TransactionCreatedEvent) {
            return KafkaTopics.TRANSACTION_CREATED;
        } else if (event instanceof TransactionCompletedEvent) {
            return KafkaTopics.TRANSACTION_COMPLETED;
        } else if (event instanceof TransactionFailedEvent) {
            return KafkaTopics.TRANSACTION_FAILED;
        } else if (event instanceof RefundProcessedEvent) {
            return KafkaTopics.REFUND_PROCESSED;
        }
        
        log.warn("Unknown event type: {}, using default topic", event.getClass().getSimpleName());
        return KafkaTopics.PAYMENT_EVENTS;
    }
}
