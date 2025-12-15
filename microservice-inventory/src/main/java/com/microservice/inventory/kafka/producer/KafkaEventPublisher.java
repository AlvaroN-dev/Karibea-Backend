package com.microservice.inventory.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.microservice.inventory.domain.events.DomainEvent;
import com.microservice.inventory.domain.port.out.EventPublisherPort;

@Component
public class KafkaEventPublisher implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private static final String TOPIC_PREFIX = "inventory.";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        String topic = TOPIC_PREFIX + event.getEventType();
        log.info("Publishing event to topic {}: {}", topic, event.getEventId());
        kafkaTemplate.send(topic, event.getAggregateId().toString(), event);
    }

    @Override
    public void publishAll(Iterable<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
