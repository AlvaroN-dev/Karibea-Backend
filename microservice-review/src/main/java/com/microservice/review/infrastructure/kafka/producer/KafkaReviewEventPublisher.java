package com.microservice.review.infrastructure.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.review.domain.events.DomainEvent;
import com.microservice.review.domain.port.out.ReviewEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka implementation of ReviewEventPublisher port.
 * Publishes domain events to Kafka topics.
 */
@Component
public class KafkaReviewEventPublisher implements ReviewEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaReviewEventPublisher.class);
    private static final String REVIEWS_TOPIC = "reviews";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaReviewEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            String key = event.getAggregateId().toString();

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(REVIEWS_TOPIC, key, eventJson);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event {} to Kafka: {}",
                            event.getEventType(), ex.getMessage());
                } else {
                    log.info("Published event {} to topic {} with offset {}",
                            event.getEventType(),
                            REVIEWS_TOPIC,
                            result.getRecordMetadata().offset());
                }
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}: {}", event.getEventType(), e.getMessage());
        }
    }
}
