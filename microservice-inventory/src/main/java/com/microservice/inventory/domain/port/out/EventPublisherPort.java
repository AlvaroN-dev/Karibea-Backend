package com.microservice.inventory.domain.port.out;

import com.microservice.inventory.domain.events.DomainEvent;

/**
 * Port OUT - Event publisher contract.
 */
public interface EventPublisherPort {

    void publish(DomainEvent event);

    void publishAll(Iterable<DomainEvent> events);
}
