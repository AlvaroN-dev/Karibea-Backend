package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.StoreSettings;
import com.microservice.store.domain.service.StoreSettingsService;

public class CreateStoreSettingsUseCase {

    private final StoreSettingsService service;

    public CreateStoreSettingsUseCase(StoreSettingsService service) {
        this.service = service;
    }

    public StoreSettings execute(StoreSettings settings) {
        return service.create(settings);
    }
}
