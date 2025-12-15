package com.microservice.identity.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Topic Configuration.
 * Defines all Kafka topics used by the identity service.
 * Only active when kafka.enabled=true.
 * 
 * Topics include:
 * - user-created-events: Published when a new user is created
 * - user-updated-events: Published when user information is updated
 * - user-deleted-events: Published when a user is deleted
 * - email-verification-events: Published when email verification is required
 * - DLQ topics for each main topic for failed message handling
 * 
 * All topics configured with:
 * - 3 partitions for parallel processing
 * - 2 replicas for fault tolerance (3-broker cluster)
 * - 7-day retention period
 */
@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaTopicConfig {

    // Topic Configuration Constants
    private static final String COMPRESSION_TYPE_CONFIG = "compression.type";
    private static final String RETENTION_MS_CONFIG = "retention.ms";
    private static final String MIN_INSYNC_REPLICAS_CONFIG = "min.insync.replicas";
    private static final String SNAPPY_COMPRESSION = "snappy";
    private static final String DLQ_RETENTION_MS = "2592000000"; // 30 days
    private static final String MIN_INSYNC_REPLICAS_VALUE = "1";
    private static final String DLQ_SUFFIX = "-dlq";


    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topics.partitions:3}")
    private Integer partitions;

    @Value("${kafka.topics.replication-factor:2}")
    private Short replicationFactor;

    @Value("${kafka.topics.retention-ms:604800000}") // 7 days
    private String retentionMs;

    @Value("${kafka.topics.user-created:user-created-events}")
    private String userCreatedTopic;

    @Value("${kafka.topics.user-updated:user-updated-events}")
    private String userUpdatedTopic;

    @Value("${kafka.topics.user-deleted:user-deleted-events}")
    private String userDeletedTopic;

    @Value("${kafka.topics.email-verification:email-verification-events}")
    private String emailVerificationTopic;

    /**
     * KafkaAdmin bean for managing topics.
     * 
     * @return KafkaAdmin configured with bootstrap servers
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * User Created Events Topic.
     * Published when a new user is created in the system.
     * 
     * @return NewTopic configuration for user-created-events
     */
    @Bean
    public NewTopic userCreatedEventsTopic() {
        return TopicBuilder.name(userCreatedTopic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, retentionMs)
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .config(MIN_INSYNC_REPLICAS_CONFIG, MIN_INSYNC_REPLICAS_VALUE)
                .build();
    }

    /**
     * User Updated Events Topic.
     * Published when user information is updated.
     * 
     * @return NewTopic configuration for user-updated-events
     */
    @Bean
    public NewTopic userUpdatedEventsTopic() {
        return TopicBuilder.name(userUpdatedTopic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, retentionMs)
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .config(MIN_INSYNC_REPLICAS_CONFIG, MIN_INSYNC_REPLICAS_VALUE)
                .build();
    }

    /**
     * User Deleted Events Topic.
     * Published when a user is deleted from the system.
     * 
     * @return NewTopic configuration for user-deleted-events
     */
    @Bean
    public NewTopic userDeletedEventsTopic() {
        return TopicBuilder.name(userDeletedTopic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, retentionMs)
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .config(MIN_INSYNC_REPLICAS_CONFIG, MIN_INSYNC_REPLICAS_VALUE)
                .build();
    }

    /**
     * Email Verification Events Topic.
     * Published when email verification is required.
     * 
     * @return NewTopic configuration for email-verification-events
     */
    @Bean
    public NewTopic emailVerificationEventsTopic() {
        return TopicBuilder.name(emailVerificationTopic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, retentionMs)
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .config(MIN_INSYNC_REPLICAS_CONFIG, MIN_INSYNC_REPLICAS_VALUE)
                .build();
    }

    /**
     * Dead Letter Queue (DLQ) for User Created Events.
     * Stores messages that failed processing after all retries.
     * 
     * @return NewTopic configuration for user-created-events-dlq
     */
    @Bean
    public NewTopic userCreatedEventsDlqTopic() {
        return TopicBuilder.name(userCreatedTopic + DLQ_SUFFIX)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, DLQ_RETENTION_MS) // 30 days for DLQ
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .build();
    }

    /**
     * Dead Letter Queue (DLQ) for User Updated Events.
     * 
     * @return NewTopic configuration for user-updated-events-dlq
     */
    @Bean
    public NewTopic userUpdatedEventsDlqTopic() {
        return TopicBuilder.name(userUpdatedTopic + DLQ_SUFFIX)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, DLQ_RETENTION_MS) // 30 days for DLQ
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .build();
    }

    /**
     * Dead Letter Queue (DLQ) for User Deleted Events.
     * 
     * @return NewTopic configuration for user-deleted-events-dlq
     */
    @Bean
    public NewTopic userDeletedEventsDlqTopic() {
        return TopicBuilder.name(userDeletedTopic + DLQ_SUFFIX)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, DLQ_RETENTION_MS) // 30 days for DLQ
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .build();
    }

    /**
     * Dead Letter Queue (DLQ) for Email Verification Events.
     * 
     * @return NewTopic configuration for email-verification-events-dlq
     */
    @Bean
    public NewTopic emailVerificationEventsDlqTopic() {
        return TopicBuilder.name(emailVerificationTopic + DLQ_SUFFIX)
                .partitions(partitions)
                .replicas(replicationFactor)
                .config(RETENTION_MS_CONFIG, DLQ_RETENTION_MS) // 30 days for DLQ
                .config(COMPRESSION_TYPE_CONFIG, SNAPPY_COMPRESSION)
                .build();
    }
}
