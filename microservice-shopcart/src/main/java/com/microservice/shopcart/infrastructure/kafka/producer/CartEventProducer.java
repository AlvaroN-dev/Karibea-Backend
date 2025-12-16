package com.microservice.shopcart.infrastructure.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.shopcart.domain.events.DomainEvent;
import com.microservice.shopcart.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka producer for publishing shopping cart domain events.
 * Implements the EventPublisherPort from the domain layer.
 */
@Component
public class CartEventProducer implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(CartEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topicPrefix;

    public CartEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${kafka.topic.prefix:shopping-cart}") String topicPrefix) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topicPrefix = topicPrefix;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String topic = buildTopicName(event);
            String key = event.getAggregateId();
            String payload = serializeEvent(event);

            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send(topic, key, payload);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event {} to topic {}: {}", 
                        event.getEventType(), topic, ex.getMessage(), ex);
                } else {
                    log.info("Successfully published event {} to topic {} at partition {} offset {}", 
                        event.getEventType(), 
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}: {}", event.getEventType(), e.getMessage(), e);
        }
    }

    /**
     * Publishes an event synchronously and waits for acknowledgment.
     */
    public void publishSync(DomainEvent event) {
        try {
            String topic = buildTopicName(event);
            String key = event.getAggregateId();
            String payload = serializeEvent(event);

            SendResult<String, String> result = kafkaTemplate.send(topic, key, payload).get();
            
            log.info("Sync published event {} to topic {} at offset {}", 
                event.getEventType(), 
                topic, 
                result.getRecordMetadata().offset());

        } catch (Exception e) {
            log.error("Failed to sync publish event {}: {}", event.getEventType(), e.getMessage(), e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }

    private String buildTopicName(DomainEvent event) {
        // Convert CamelCase to kebab-case: CartCreated -> cart-created
        String eventName = event.getEventType()
            .replaceAll("([a-z])([A-Z])", "$1-$2")
            .toLowerCase();
        return topicPrefix + "." + eventName;
    }

    private String serializeEvent(DomainEvent event) throws JsonProcessingException {
        EventEnvelope envelope = new EventEnvelope(
            event.getEventId(),
            event.getEventType(),
            event.getAggregateId(),
            event.getOccurredAt().toString(),
            event
        );
        return objectMapper.writeValueAsString(envelope);
    }

    /**
     * Event envelope for Kafka messages.
     */
    private record EventEnvelope(
        String eventId,
        String eventType,
        String aggregateId,
        String occurredAt,
        Object payload
    ) {}
}
