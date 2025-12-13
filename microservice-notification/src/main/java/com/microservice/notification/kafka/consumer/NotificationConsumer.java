package com.microservice.notification.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(topics = "notification.send", groupId = "notification-group")
    public void listen(String message) {
        logger.info("Received message: {}", message);
        // Logic to process message and call Use Cases would go here
    }
}
