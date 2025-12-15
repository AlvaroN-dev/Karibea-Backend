package com.microservice.search.infrastructure.kafka.producer;

import com.microservice.search.domain.events.DomainEvent;
import com.microservice.search.domain.port.out.EventPublisherPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publisher de eventos a Kafka.
 */
@Component
public class KafkaEventPublisher implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private static final String DEFAULT_TOPIC = "search-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DomainEvent event) {
        publish(DEFAULT_TOPIC, event);
    }

    @Override
    public void publish(String topic, DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(topic, event.eventId().toString(), payload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event {} to {}: {}",
                                    event.eventType(), topic, ex.getMessage());
                        } else {
                            log.debug("Event published to {}: {} - {}",
                                    topic, event.eventType(), event.eventId());
                        }
                    });

        } catch (Exception e) {
            log.error("Error serializing event {}: {}", event.eventType(), e.getMessage());
        }
    }
}
