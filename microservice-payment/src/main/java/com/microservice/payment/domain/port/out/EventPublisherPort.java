package com.microservice.payment.domain.port.out;

import com.microservice.payment.domain.events.DomainEvent;

/**
 * Port OUT - Contract for publishing domain events.
 * 
 * <p>
 * <b>Why event publisher in domain:</b>
 * </p>
 * <ul>
 * <li>Domain orchestrates when events should be published</li>
 * <li>Infrastructure handles how (Kafka, RabbitMQ, etc.)</li>
 * <li>Decouples domain from messaging infrastructure</li>
 * </ul>
 */
public interface EventPublisherPort {

    /**
     * Publishes a domain event.
     * 
     * @param event the domain event to publish
     */
    void publish(DomainEvent event);

    /**
     * Publishes a domain event to a specific topic.
     * 
     * @param event the domain event to publish
     * @param topic the topic to publish to
     */
    void publish(DomainEvent event, String topic);
}
