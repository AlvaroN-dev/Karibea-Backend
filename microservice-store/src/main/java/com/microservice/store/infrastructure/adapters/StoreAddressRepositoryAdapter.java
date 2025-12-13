package com.microservice.store.infrastructure.adapters;

import com.microservice.store.domain.models.StoreAddress;
import com.microservice.store.domain.port.StoreAddressRepositoryPort;
import com.microservice.store.infrastructure.entities.StoreAddressEntity;
import com.microservice.store.infrastructure.repositories.StoreAddressJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StoreAddressRepositoryAdapter implements StoreAddressRepositoryPort {

    private final StoreAddressJpaRepository repository;

    public StoreAddressRepositoryAdapter(StoreAddressJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public StoreAddress save(StoreAddress address) {
        return toDomain(repository.save(toEntity(address)));
    }

    @Override
    public Optional<StoreAddress> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<StoreAddress> findByStoreId(Long storeId) {
        return repository.findByStoreId(storeId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public StoreAddress update(StoreAddress address) {
        return toDomain(repository.save(toEntity(address)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private StoreAddressEntity toEntity(StoreAddress a) {
        StoreAddressEntity e = new StoreAddressEntity();
        e.setId(a.getId());
        e.setStoreId(a.getId());
        e.setAddressType(a.getAddressType());
        e.setStreetAddress(a.getStreetAddress());
        e.setCity(a.getCity());
        e.setState(a.getState());
        e.setPostalCode(a.getPostalCode());
        e.setCountry(a.getCountry());
        e.setPrimary(a.isPrimary());
        return e;
    }

    private StoreAddress toDomain(StoreAddressEntity e) {
        StoreAddress a = new StoreAddress();
        a.setId(e.getId());
        a.setId(e.getStoreId());
        a.setAddressType(e.getAddressType());
        a.setStreetAddress(e.getStreetAddress());
        a.setCity(e.getCity());
        a.setState(e.getState());
        a.setPostalCode(e.getPostalCode());
        a.setCountry(e.getCountry());
        a.setPrimary(e.isPrimary());
        return a;
    }
}
