package com.microservice.store.domain.service;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.port.StoreRepositoryPort;

import java.util.List;

public class StoreService {

    private final StoreRepositoryPort repository;

    public StoreService(StoreRepositoryPort repository) {
        this.repository = repository;
    }

    /* CREATE */
    public Store create(Store store) {
        return repository.save(store);
    }

    /* READ */
    public Store getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    public Store getByOwner(String externalOwnerUserId) {
        return repository.findByExternalOwnerUserId(externalOwnerUserId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    public List<Store> getAllActive() {
        return repository.findAllActive();
    }

    /* UPDATE */
    public Store update(Long id, Store store) {
        Store existing = getById(id);

        existing.setName(store.getName());
        existing.setDescription(store.getDescription());
        existing.setEmail(store.getEmail());
        existing.setPhone(store.getPhone());
        existing.setLogoUrl(store.getLogoUrl());
        existing.setBannerUrl(store.getBannerUrl());

        return repository.update(existing);
    }

    /* DELETE (soft) */
    public void delete(Long id) {
        repository.softDelete(id);
    }
}
