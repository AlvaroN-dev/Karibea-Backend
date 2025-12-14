package com.microservice.store.domain.port;

import com.microservice.store.domain.event.StoreCreatedEvent;

public interface StoreEventPublisherPort {
    void publish(StoreCreatedEvent event);
}
