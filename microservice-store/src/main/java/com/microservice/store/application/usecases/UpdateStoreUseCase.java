package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.service.StoreService;

public class UpdateStoreUseCase {

    private final StoreService service;

    public UpdateStoreUseCase(StoreService service) {
        this.service = service;
    }

    public Store execute(Long id, Store store) {
        return service.update(id, store);
    }
}
