package com.microservice.user.domain.port.out;

import com.microservice.user.domain.events.DomainEvent;

/**
 * Puerto saliente - Publicador de eventos (Kafka)
 */
public interface EventPublisherPort {
    
    void publish(DomainEvent event);
    
    void publish(String topic, DomainEvent event);
}
