package com.microservice.store.infrastructure.adapters;

import com.microservice.store.domain.models.Store;
import com.microservice.store.domain.port.StoreRepositoryPort;
import com.microservice.store.infrastructure.entities.StoreEntity;
import com.microservice.store.infrastructure.repositories.StoreJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StoreRepositoryAdapter implements StoreRepositoryPort {

    private final StoreJpaRepository repository;

    public StoreRepositoryAdapter(StoreJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Store save(Store store) {
        return toDomain(repository.save(toEntity(store)));
    }

    @Override
    public Optional<Store> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Store> findByExternalOwnerUserId(String externalOwnerUserId) {
        return repository.findByExternalOwnerUserId(externalOwnerUserId)
                .map(this::toDomain);
    }

    @Override
    public List<Store> findAllActive() {
        return repository.findByDeletedFalse()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Store update(Store store) {
        return toDomain(repository.save(toEntity(store)));
    }

    @Override
    public void softDelete(Long id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeleted(true);
            repository.save(entity);
        });
    }

    private StoreEntity toEntity(Store store) {
        StoreEntity entity = new StoreEntity();
        entity.setId(store.getId());
        entity.setName(store.getName());
        entity.setDescription(store.getDescription());
        entity.setEmail(store.getEmail());
        entity.setPhone(store.getPhone());
        entity.setLogoUrl(store.getLogoUrl());
        entity.setBannerUrl(store.getBannerUrl());
        entity.setDeleted(store.isDeleted());
        entity.setCreatedAt(store.getCreatedAt());
        entity.setUpdatedAt(store.getUpdatedAt());
        entity.setDeletedAt(store.getDeletedAt());
        return entity;
    }

    private Store toDomain(StoreEntity entity) {
        Store store = new Store();
        store.setId(entity.getId());
        store.setName(entity.getName());
        store.setDescription(entity.getDescription());
        store.setEmail(entity.getEmail());
        store.setPhone(entity.getPhone());
        store.setLogoUrl(entity.getLogoUrl());
        store.setBannerUrl(entity.getBannerUrl());
        store.setDeleted(entity.isDeleted());
        store.setCreatedAt(entity.getCreatedAt());
        store.setUpdatedAt(entity.getUpdatedAt());
        store.setDeletedAt(entity.getDeletedAt());
        return store;
    }
}
