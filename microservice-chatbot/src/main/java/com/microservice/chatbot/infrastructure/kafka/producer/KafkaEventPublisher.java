package com.microservice.chatbot.infrastructure.kafka.producer;

import com.microservice.chatbot.domain.events.EscalationCreatedEvent;
import com.microservice.chatbot.domain.events.MessageSentEvent;
import com.microservice.chatbot.infrastructure.kafka.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka event publisher for domain events.
 * Location: infrastructure/kafka/producer - Event publishing component.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @EventListener
    public void handleMessageSent(MessageSentEvent event) {
        log.debug("Publishing MessageSentEvent to Kafka: {}", event.getMessageId());
        try {
            kafkaTemplate.send(KafkaTopicConfig.MESSAGE_TOPIC, event.getConversationId().toString(), event);
            log.info("MessageSentEvent published successfully: {}", event.getMessageId());
        } catch (Exception e) {
            log.error("Failed to publish MessageSentEvent: {}", e.getMessage(), e);
        }
    }

    @EventListener
    public void handleEscalationCreated(EscalationCreatedEvent event) {
        log.debug("Publishing EscalationCreatedEvent to Kafka: {}", event.getEscalationId());
        try {
            kafkaTemplate.send(KafkaTopicConfig.ESCALATION_TOPIC, event.getConversationId().toString(), event);
            log.info("EscalationCreatedEvent published successfully: {}", event.getEscalationId());
        } catch (Exception e) {
            log.error("Failed to publish EscalationCreatedEvent: {}", e.getMessage(), e);
        }
    }
}
