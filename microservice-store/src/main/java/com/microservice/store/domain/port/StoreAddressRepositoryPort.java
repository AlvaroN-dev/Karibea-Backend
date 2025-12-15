package com.microservice.store.domain.port;

import com.microservice.store.domain.models.StoreAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreAddressRepositoryPort {

    StoreAddress save(StoreAddress address);

    Optional<StoreAddress> findById(UUID id);

    List<StoreAddress> findByStoreId(UUID storeId);

    void delete(UUID id);

    StoreAddress update(StoreAddress address);
}
