package com.microservice.notification.domain.port.out;

import com.microservice.notification.domain.events.DomainEvent;

/**
 * Port for publishing domain events to an external messaging system.
 * This is an outbound port in the hexagonal architecture.
 */
public interface EventPublisherPort {

    /**
     * Publishes a domain event to the default topic.
     *
     * @param event the domain event to publish
     */
    void publish(DomainEvent event);

    /**
     * Publishes a domain event to a specific topic.
     *
     * @param topic the topic to publish to
     * @param event the domain event to publish
     */
    void publish(String topic, DomainEvent event);

    /**
     * Publishes multiple domain events.
     *
     * @param events the domain events to publish
     */
    void publishAll(Iterable<DomainEvent> events);
}
