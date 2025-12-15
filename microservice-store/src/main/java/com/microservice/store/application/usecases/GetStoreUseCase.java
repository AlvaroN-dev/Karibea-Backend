package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.service.StoreService;
import java.util.UUID;

public class GetStoreUseCase {

    private final StoreService service;

    public GetStoreUseCase(StoreService service) {
        this.service = service;
    }

    public Store byId(UUID id) {
        return service.getById(id);
    }

    public Store byOwner(UUID ownerId) {
        return service.getByOwner(ownerId);
    }
}
