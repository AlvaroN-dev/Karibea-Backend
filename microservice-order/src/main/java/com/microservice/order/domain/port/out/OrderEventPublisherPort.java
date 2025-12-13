package com.microservice.order.domain.port.out;

import com.microservice.order.domain.events.DomainEvent;

import java.util.List;

/**
 * Outbound port for publishing domain events.
 */
public interface OrderEventPublisherPort {

    void publish(DomainEvent event);

    void publishAll(List<DomainEvent> events);
}
