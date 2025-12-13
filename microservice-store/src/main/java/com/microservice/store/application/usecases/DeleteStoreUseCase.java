package com.microservice.store.application.usecases;

import com.microservice.store.domain.service.StoreService;

public class DeleteStoreUseCase {

    private final StoreService service;

    public DeleteStoreUseCase(StoreService service) {
        this.service = service;
    }

    public void execute(Long id) {
        service.delete(id);
    }
}
