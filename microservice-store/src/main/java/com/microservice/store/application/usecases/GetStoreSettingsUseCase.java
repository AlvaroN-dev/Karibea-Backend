package com.microservice.store.application.usecases;

import com.microservice.store.domain.models.StoreSettings;
import com.microservice.store.domain.service.StoreSettingsService;

public class GetStoreSettingsUseCase {

    private final StoreSettingsService service;

    public GetStoreSettingsUseCase(StoreSettingsService service) {
        this.service = service;
    }

    public StoreSettings execute(Long storeId) {
        return service.getByStoreId(storeId);
    }
}
