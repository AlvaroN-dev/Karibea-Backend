package com.microservice.store.domain.port;

import com.microservice.store.domain.models.Store;

import java.util.List;
import java.util.Optional;

public interface StoreRepositoryPort {

    Store save(Store store);

    Optional<Store> findById(Long id);

    Optional<Store> findByExternalOwnerUserId(String externalOwnerUserId);

    List<Store> findAllActive();

    Store update(Store store);

    void softDelete(Long id);
}
