package com.microservice.store.domain.port;

import com.microservice.store.domain.models.StoreAddress;

import java.util.List;
import java.util.Optional;

public interface StoreAddressRepositoryPort {

    StoreAddress save(StoreAddress address);

    Optional<StoreAddress> findById(Long id);

    List<StoreAddress> findByStoreId(Long storeId);

    void delete(Long id);

    StoreAddress update(StoreAddress address);
}
