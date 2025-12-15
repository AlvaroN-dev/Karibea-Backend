package com.microservice.marketing.infrastructure.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventConsumer.class);

    @KafkaListener(topics = "marketing.coupon.used", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCouponUsed(Object message) {
        logger.info("Received Coupon Used Event: {}", message);
        // Implementation for reaction would go here
    }

    @KafkaListener(topics = "marketing.promotion.created", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePromotionCreated(Object message) {
        logger.info("Received Promotion Created Event: {}", message);
    }
}
