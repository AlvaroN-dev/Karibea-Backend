package com.microservice.shopcart.domain.port.out;

import com.microservice.shopcart.domain.events.DomainEvent;

/**
 * Output port for publishing domain events.
 */
public interface EventPublisherPort {
    
    /**
     * Publishes a domain event.
     *
     * @param event The domain event to publish
     */
    void publish(DomainEvent event);
}
