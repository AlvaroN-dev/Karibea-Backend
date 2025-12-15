package com.microservice.payment.infrastructure.kafka.config;

/**
 * Constants for Kafka topic names.
 */
public final class KafkaTopics {

    private KafkaTopics() {
    }

    // Payment events
    public static final String PAYMENT_EVENTS = "payment-events";
    public static final String TRANSACTION_CREATED = "payment.transaction.created";
    public static final String TRANSACTION_COMPLETED = "payment.transaction.completed";
    public static final String TRANSACTION_FAILED = "payment.transaction.failed";
    public static final String REFUND_PROCESSED = "payment.refund.processed";

    // Incoming events from other services
    public static final String ORDER_EVENTS = "order-events";
    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_CANCELLED = "order.cancelled";
}
