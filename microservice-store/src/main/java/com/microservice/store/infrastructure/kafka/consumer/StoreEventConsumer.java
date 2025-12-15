package com.microservice.store.infrastructure.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StoreEventConsumer {

    private final Logger logger = LoggerFactory.getLogger(StoreEventConsumer.class);

    @KafkaListener(topics = "store-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Object event) {
        logger.info("Event consumed: {}", event);
    }
}
