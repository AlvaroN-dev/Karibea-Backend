package com.microservice.chatbot.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka topic configuration.
 * Location: infrastructure/kafka/config - Kafka topic definitions.
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    public static final String MESSAGE_TOPIC = "chatbot.messages";
    public static final String ESCALATION_TOPIC = "chatbot.escalations";
    public static final String CONVERSATION_TOPIC = "chatbot.conversations";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic messagesTopic() {
        return TopicBuilder.name(MESSAGE_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic escalationsTopic() {
        return TopicBuilder.name(ESCALATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic conversationsTopic() {
        return TopicBuilder.name(CONVERSATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
