package com.microservice.store.application.usecases;

import com.microservice.store.domain.service.StoreSettingsService;
import java.util.UUID;

public class DeleteStoreSettingsUseCase {

    private final StoreSettingsService service;

    public DeleteStoreSettingsUseCase(StoreSettingsService service) {
        this.service = service;
    }

    public void execute(UUID storeId) {
        service.delete(storeId);
    }
}
