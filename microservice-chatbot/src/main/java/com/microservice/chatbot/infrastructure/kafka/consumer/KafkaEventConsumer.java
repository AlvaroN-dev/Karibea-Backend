package com.microservice.chatbot.infrastructure.kafka.consumer;

import com.microservice.chatbot.infrastructure.kafka.config.KafkaTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka event consumer for incoming events.
 * Location: infrastructure/kafka/consumer - Event consuming component.
 */
@Slf4j
@Component
public class KafkaEventConsumer {

    /**
     * Listens for message events from other services.
     * Extend this method to handle external events.
     */
    @KafkaListener(topics = KafkaTopicConfig.MESSAGE_TOPIC, groupId = "${spring.kafka.consumer.group-id:chatbot-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeMessageEvent(Object event) {
        log.debug("Received message event: {}", event);
        // Process incoming event
        // This can be extended to handle events from other services
    }

    /**
     * Listens for escalation events from other services.
     */
    @KafkaListener(topics = KafkaTopicConfig.ESCALATION_TOPIC, groupId = "${spring.kafka.consumer.group-id:chatbot-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeEscalationEvent(Object event) {
        log.debug("Received escalation event: {}", event);
        // Process incoming escalation event
        // This can be extended to handle agent assignment notifications, etc.
    }
}
