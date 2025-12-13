package com.microservice.store.domain.port;

import com.microservice.store.domain.models.StoreSettings;

import java.util.Optional;

public interface StoreSettingsRepositoryPort {

    StoreSettings save(StoreSettings settings);

    Optional<StoreSettings> findByStoreId(Long storeId);

    void delete(Long storeId);

    StoreSettings update(StoreSettings settings);
}
