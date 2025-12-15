package com.microservice.store.domain.service;

import com.microservice.store.domain.models.StoreAddress;
import com.microservice.store.domain.port.StoreAddressRepositoryPort;

import java.util.List;
import java.util.UUID;

public class StoreAddressService {

    private final StoreAddressRepositoryPort repository;

    public StoreAddressService(StoreAddressRepositoryPort repository) {
        this.repository = repository;
    }

    public StoreAddress create(StoreAddress address) {
        return repository.save(address);
    }

    public List<StoreAddress> getAllByStoreId(UUID storeId) {
        return repository.findByStoreId(storeId);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }
}
