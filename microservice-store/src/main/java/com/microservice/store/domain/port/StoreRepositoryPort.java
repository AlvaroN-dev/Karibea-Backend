package com.microservice.store.domain.port;

import com.microservice.store.domain.models.Store;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepositoryPort {

    Store save(Store store);

    Optional<Store> findById(UUID id);

    Optional<Store> findByExternalOwnerUserId(UUID externalOwnerUserId);

    List<Store> findAllActive();

    Store update(Store store);

    void softDelete(UUID id);
}
