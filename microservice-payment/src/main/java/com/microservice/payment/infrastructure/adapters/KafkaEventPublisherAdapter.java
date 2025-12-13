package com.microservice.payment.infrastructure.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
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

/**
 * Adapter implementing EventPublisherPort for Kafka.
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
public class KafkaEventPublisherAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisherAdapter.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisherAdapter(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DomainEvent event) {
        String topic = resolveTopicForEvent(event);
        publish(event, topic);
    }

    @Override
    public void publish(DomainEvent event, String topic) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            String key = event.getAggregateId().toString();

            kafkaTemplate.send(topic, key, payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event {} to topic {}",
                                    event.getEventType(), topic, ex);
                        } else {
                            log.info("Published event {} to topic {} with offset {}",
                                    event.getEventType(), topic,
                                    result.getRecordMetadata().offset());
                        }
                    });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", event.getEventType(), e);
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

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
        return KafkaTopics.PAYMENT_EVENTS;
    }
}
