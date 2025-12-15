package com.microservice.catalog.domain.port.out;

import com.microservice.catalog.domain.events.DomainEvent;

/**
 * Output port for publishing domain events.
 * This abstracts the event publishing mechanism (e.g., Kafka).
 */
public interface EventPublisher {

    /**
     * Publishes a domain event.
     *
     * @param event the domain event to publish
     */
    void publish(DomainEvent event);
}
