package com.microservice.store.domain.port;

import com.microservice.store.domain.models.StoreSettings;

import java.util.Optional;
import java.util.UUID;

public interface StoreSettingsRepositoryPort {

    StoreSettings save(StoreSettings settings);

    Optional<StoreSettings> findByStoreId(UUID storeId);

    void delete(UUID storeId);

    StoreSettings update(StoreSettings settings);
}
