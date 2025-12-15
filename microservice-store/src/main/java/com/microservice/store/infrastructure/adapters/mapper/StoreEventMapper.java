package com.microservice.store.infrastructure.adapters.mapper;

import com.microservice.store.domain.event.StoreCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class StoreEventMapper {

    public StoreCreatedEvent toEvent(com.microservice.store.domain.models.Store store) {
        return new StoreCreatedEvent(
                store.getId(),
                store.getExternalOwnerUserId(),
                store.getName(),
                store.getEmail(),
                store.getCreatedAt());
    }
}
