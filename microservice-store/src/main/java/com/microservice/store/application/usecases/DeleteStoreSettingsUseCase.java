package com.microservice.store.application.usecases;

import com.microservice.store.domain.service.StoreSettingsService;

public class DeleteStoreSettingsUseCase {

    private final StoreSettingsService service;

    public DeleteStoreSettingsUseCase(StoreSettingsService service) {
        this.service = service;
    }

    public void execute(Long storeId) {
        service.delete(storeId);
    }
}
