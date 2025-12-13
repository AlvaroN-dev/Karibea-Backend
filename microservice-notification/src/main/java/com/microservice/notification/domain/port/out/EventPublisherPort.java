package com.microservice.notification.domain.port.out;

import com.microservice.notification.domain.events.DomainEvent;

public interface EventPublisherPort {

    void publish(DomainEvent event);

    void publishAll(Iterable<DomainEvent> events);
}
