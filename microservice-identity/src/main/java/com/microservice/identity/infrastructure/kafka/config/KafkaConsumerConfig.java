package com.microservice.identity.infrastructure.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Consumer Configuration.
 * Configures the Kafka consumer with optimal settings for reliability and
 * concurrency.
 * Only active when kafka.enabled=true.
 * 
 * Key features:
 * - Manual offset commit for at-least-once delivery
 * - Concurrent consumers for parallel processing
 * - Error handling deserializer for fault tolerance
 * - JSON deserialization for event payloads
 */
@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    @Value("${kafka.consumer.enable-auto-commit:false}")
    private Boolean enableAutoCommit;

    @Value("${kafka.consumer.max-poll-records:10}")
    private Integer maxPollRecords;

    @Value("${kafka.consumer.fetch-min-size:1}")
    private Integer fetchMinSize;

    @Value("${kafka.consumer.fetch-max-wait-ms:500}")
    private Integer fetchMaxWaitMs;

    @Value("${kafka.listener.concurrency:3}")
    private Integer concurrency;

    @Value("${kafka.listener.poll-timeout:3000}")
    private Long pollTimeout;

    /**
     * Creates the consumer factory with configured properties.
     * Uses ErrorHandlingDeserializer to handle deserialization errors gracefully.
     * 
     * @return ConsumerFactory configured for String keys and Object values
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // Bootstrap servers
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Consumer group
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // Deserializers with error handling
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // Offset management
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);

        // Performance settings
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        configProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinSize);
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);

        // JSON deserializer settings
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.microservice.identity.domain.events");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.microservice.identity.domain.events.DomainEvent");

        // Isolation level for transactional reads
        configProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        // Session timeout
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    /**
     * Creates the listener container factory for @KafkaListener annotations.
     * Configured with manual acknowledgment and concurrent consumers.
     * 
     * @return ConcurrentKafkaListenerContainerFactory configured for concurrent
     *         processing
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        // Concurrency: number of consumer threads
        factory.setConcurrency(concurrency);

        // Manual acknowledgment for at-least-once delivery
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // Poll timeout
        factory.getContainerProperties().setPollTimeout(pollTimeout);

        // Enable batch listening (optional, can be overridden per listener)
        factory.setBatchListener(false);

        return factory;
    }

    /**
     * Creates a batch listener container factory for batch processing.
     * Useful for processing multiple events together for efficiency.
     * 
     * @return ConcurrentKafkaListenerContainerFactory configured for batch
     *         processing
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> batchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(pollTimeout);

        // Enable batch listening
        factory.setBatchListener(true);

        return factory;
    }
}
