package com.microservice.store.infrastructure.adapters;

import com.microservice.store.domain.models.StoreSettings;
import com.microservice.store.domain.port.StoreSettingsRepositoryPort;
import com.microservice.store.infrastructure.entities.StoreSettingsEntity;
import com.microservice.store.infrastructure.repositories.StoreSettingsJpaRepository;
import java.util.UUID;

public class StoreSettingsRepositoryAdapter implements StoreSettingsRepositoryPort {

    private final StoreSettingsJpaRepository repository;

    public StoreSettingsRepositoryAdapter(StoreSettingsJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public StoreSettings save(StoreSettings settings) {
        return toDomain(repository.save(toEntity(settings)));
    }

    @Override
    public StoreSettings update(StoreSettings settings) {
        return toDomain(repository.save(toEntity(settings)));
    }

    @Override
    public java.util.Optional<StoreSettings> findByStoreId(UUID storeId) {
        return repository.findByStoreId(storeId).map(this::toDomain);
    }

    @Override
    public void delete(UUID storeId) {
        repository.deleteByStoreId(storeId);
    }

    private StoreSettingsEntity toEntity(StoreSettings s) {
        StoreSettingsEntity e = new StoreSettingsEntity();
        e.setId(s.getId());
        e.setStoreId(s.getId());
        e.setReturnPolicy(s.getReturnPolicy());
        e.setShippingPolicy(s.getShippingPolicy());
        e.setMinOrderAmount(s.getMinOrderAmount());
        e.setAcceptsReturns(s.isAcceptsReturns());
        e.setReturnWindowDays(s.getReturnWindowDays());
        return e;
    }

    private StoreSettings toDomain(StoreSettingsEntity e) {
        StoreSettings s = new StoreSettings();
        s.setId(e.getId());
        s.setId(e.getStoreId());
        s.setReturnPolicy(e.getReturnPolicy());
        s.setShippingPolicy(e.getShippingPolicy());
        s.setMinOrderAmount(e.getMinOrderAmount());
        s.setAcceptsReturns(e.isAcceptsReturns());
        s.setReturnWindowDays(e.getReturnWindowDays());
        return s;
    }
}
