package com.microservice.store.infrastructure.kafka;

import com.microservice.store.domain.event.StoreCreatedEvent;
import com.microservice.store.domain.port.StoreEventPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaStoreEventPublisherAdapter implements StoreEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaStoreEventPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(StoreCreatedEvent event) {
        kafkaTemplate.send("store-created-topic", event);
    }
}
