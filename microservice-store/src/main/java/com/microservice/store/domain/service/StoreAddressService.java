package com.microservice.store.domain.service;

import com.microservice.store.domain.models.StoreAddress;
import com.microservice.store.domain.port.StoreAddressRepositoryPort;

import java.util.List;

public class StoreAddressService {

    private final StoreAddressRepositoryPort repository;

    public StoreAddressService(StoreAddressRepositoryPort repository) {
        this.repository = repository;
    }

    public StoreAddress create(StoreAddress address) {
        return repository.save(address);
    }

    public List<StoreAddress> getAllByStoreId(Long storeId) {
        return repository.findByStoreId(storeId);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
