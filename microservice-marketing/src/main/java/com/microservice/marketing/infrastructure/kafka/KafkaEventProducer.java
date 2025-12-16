package com.microservice.marketing.infrastructure.kafka;

import com.microservice.marketing.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, DomainEvent event) {
        logger.info("Publishing event {} to topic {}", event.getClass().getSimpleName(), topic);
        kafkaTemplate.send(topic, event);
    }
}
