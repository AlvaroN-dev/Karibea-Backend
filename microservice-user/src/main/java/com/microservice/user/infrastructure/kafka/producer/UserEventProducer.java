package com.microservice.user.infrastructure.kafka.producer;

import com.microservice.user.domain.events.DomainEvent;
import com.microservice.user.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Productor de eventos Kafka
 * Implementa el puerto saliente para publicar eventos de dominio
 */
@Component
public class UserEventProducer implements EventPublisherPort {
    
    private static final Logger log = LoggerFactory.getLogger(UserEventProducer.class);
    private static final String DEFAULT_TOPIC = "user.profile.events";
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Override
    public void publish(DomainEvent event) {
        publish(DEFAULT_TOPIC, event);
    }
    
    @Override
    public void publish(String topic, DomainEvent event) {
        log.info("Publishing event {} to topic {}", event.getEventType(), topic);
        
        kafkaTemplate.send(topic, event.getAggregateId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event: {}", event.getEventType(), ex);
                } else {
                    log.debug("Event published successfully: {} to partition {}", 
                        event.getEventType(), 
                        result.getRecordMetadata().partition());
                }
            });
    }
}
