package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.service.StoreService;

public class CreateStoreUseCase {

    private final StoreService service;

    public CreateStoreUseCase(StoreService service) {
        this.service = service;
    }

    public Store execute(Store store) {
        return service.create(store);
    }
}
