package com.microservice.review.domain.port.out;

import com.microservice.review.domain.events.DomainEvent;

/**
 * Output port for publishing domain events.
 */
public interface ReviewEventPublisher {

    /**
     * Publishes a domain event.
     */
    void publish(DomainEvent event);
}
