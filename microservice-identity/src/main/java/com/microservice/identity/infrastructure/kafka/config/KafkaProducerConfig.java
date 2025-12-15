package com.microservice.identity.infrastructure.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Producer Configuration.
 * Configures the Kafka producer with optimal settings for reliability and
 * performance.
 * Only active when kafka.enabled=true.
 * 
 * Key features:
 * - Idempotent producer for exactly-once semantics
 * - Snappy compression for network efficiency
 * - Retry configuration for fault tolerance
 * - JSON serialization for event payloads
 */
@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.acks:all}")
    private String acks;

    @Value("${kafka.producer.retries:3}")
    private Integer retries;

    @Value("${kafka.producer.batch-size:16384}")
    private Integer batchSize;

    @Value("${kafka.producer.linger-ms:10}")
    private Integer lingerMs;

    @Value("${kafka.producer.buffer-memory:33554432}")
    private Long bufferMemory;

    @Value("${kafka.producer.compression-type:snappy}")
    private String compressionType;

    @Value("${kafka.producer.max-in-flight-requests-per-connection:5}")
    private Integer maxInFlightRequestsPerConnection;

    @Value("${kafka.producer.enable-idempotence:true}")
    private Boolean enableIdempotence;

    /**
     * Creates the producer factory with configured properties.
     * 
     * @return ProducerFactory configured for String keys and Object values
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // Bootstrap servers
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Serializers
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Reliability settings
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);

        // Performance settings
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerConnection);

        // JSON serializer settings
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        // Timeout settings
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates the KafkaTemplate for sending messages.
     * 
     * @return KafkaTemplate configured with the producer factory
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
