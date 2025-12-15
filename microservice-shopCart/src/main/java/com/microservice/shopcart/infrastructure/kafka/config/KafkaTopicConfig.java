package com.microservice.shopcart.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic configuration for the Shopping Cart microservice.
 * Defines all topics that this service produces to.
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.prefix:shopping-cart}")
    private String topicPrefix;

    @Value("${kafka.topic.partitions:3}")
    private int partitions;

    @Value("${kafka.topic.replicas:1}")
    private short replicas;

    @Bean
    public NewTopic cartCreatedTopic() {
        return TopicBuilder.name(topicPrefix + ".cart-created")
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public NewTopic itemAddedTopic() {
        return TopicBuilder.name(topicPrefix + ".item-added-to-cart")
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public NewTopic itemRemovedTopic() {
        return TopicBuilder.name(topicPrefix + ".item-removed-from-cart")
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public NewTopic itemQuantityUpdatedTopic() {
        return TopicBuilder.name(topicPrefix + ".item-quantity-updated")
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public NewTopic couponAppliedTopic() {
        return TopicBuilder.name(topicPrefix + ".coupon-applied-to-cart")
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public NewTopic cartExpiredTopic() {
        return TopicBuilder.name(topicPrefix + ".cart-expired")
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public NewTopic cartConvertedTopic() {
        return TopicBuilder.name(topicPrefix + ".cart-converted")
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }
}
