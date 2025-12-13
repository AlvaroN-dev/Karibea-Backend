package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.service.StoreService;

public class GetStoreUseCase {

    private final StoreService service;

    public GetStoreUseCase(StoreService service) {
        this.service = service;
    }

    public Store byId(Long id) {
        return service.getById(id);
    }

    public Store byOwner(String ownerId) {
        return service.getByOwner(ownerId);
    }
}
