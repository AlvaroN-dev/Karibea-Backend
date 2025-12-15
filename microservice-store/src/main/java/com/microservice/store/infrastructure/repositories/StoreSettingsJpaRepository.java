package com.microservice.store.infrastructure.repositories;

import com.microservice.store.infrastructure.entities.StoreSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreSettingsJpaRepository extends JpaRepository<StoreSettingsEntity, UUID> {
    Optional<StoreSettingsEntity> findByStoreId(UUID storeId);
    void deleteByStoreId(UUID storeId);
}
