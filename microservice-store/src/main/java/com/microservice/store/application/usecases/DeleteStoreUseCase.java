package com.microservice.store.application.usecases;

import com.microservice.store.domain.service.StoreService;
import java.util.UUID;

public class DeleteStoreUseCase {

    private final StoreService service;

    public DeleteStoreUseCase(StoreService service) {
        this.service = service;
    }

    public void execute(UUID id) {
        service.delete(id);
    }
}
