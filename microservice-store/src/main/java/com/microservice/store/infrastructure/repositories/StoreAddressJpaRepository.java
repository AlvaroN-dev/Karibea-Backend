package com.microservice.store.infrastructure.repositories;

import com.microservice.store.infrastructure.entities.StoreAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreAddressJpaRepository extends JpaRepository<StoreAddressEntity, UUID> {
    List<StoreAddressEntity> findByStoreId(UUID storeId);
}
