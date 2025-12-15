package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.service.StoreService;

public class CreateStoreUseCase {

    private final StoreService service;
    private final com.microservice.store.domain.port.UserGatewayPort userGatewayPort;
    private final com.microservice.store.domain.port.StoreEventPublisherPort storeEventPublisherPort;
    private final com.microservice.store.infrastructure.adapters.mapper.StoreEventMapper storeEventMapper;

    public CreateStoreUseCase(
            StoreService service,
            com.microservice.store.domain.port.UserGatewayPort userGatewayPort,
            com.microservice.store.domain.port.StoreEventPublisherPort storeEventPublisherPort,
            com.microservice.store.infrastructure.adapters.mapper.StoreEventMapper storeEventMapper) {
        this.service = service;
        this.userGatewayPort = userGatewayPort;
        this.storeEventPublisherPort = storeEventPublisherPort;
        this.storeEventMapper = storeEventMapper;
    }

    public Store execute(Store store) {
        if (!userGatewayPort.exists(store.getExternalOwnerUserId())) {
            throw new RuntimeException("User not found: " + store.getExternalOwnerUserId());
        }
        Store createdStore = service.create(store);
        storeEventPublisherPort.publish(storeEventMapper.toEvent(createdStore));
        return createdStore;
    }
}
