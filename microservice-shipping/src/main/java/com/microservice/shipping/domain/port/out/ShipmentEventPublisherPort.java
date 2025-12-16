package com.microservice.shipping.domain.port.out;

import com.microservice.shipping.domain.events.DomainEvent;

import java.util.List;

/**
 * Outbound port for publishing domain events.
 */
public interface ShipmentEventPublisherPort {

    void publish(DomainEvent event);

    void publishAll(List<DomainEvent> events);
}
