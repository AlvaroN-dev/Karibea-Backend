package com.microservice.store.domain.service;

import com.microservice.store.domain.models.StoreSettings;
import com.microservice.store.domain.port.StoreSettingsRepositoryPort;

import java.util.UUID;

public class StoreSettingsService {

    private final StoreSettingsRepositoryPort repository;

    public StoreSettingsService(StoreSettingsRepositoryPort repository) {
        this.repository = repository;
    }

    public StoreSettings create(StoreSettings settings) {
        return repository.save(settings);
    }

    public StoreSettings getByStoreId(UUID storeId) {
        return repository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("Store settings not found"));
    }

    public void delete(UUID storeId) {
        repository.delete(storeId);
    }
}
