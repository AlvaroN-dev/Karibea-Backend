package com.microservice.marketing.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic couponUsedTopic() {
        return TopicBuilder.name("marketing.coupon.used")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic promotionCreatedTopic() {
        return TopicBuilder.name("marketing.promotion.created")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
