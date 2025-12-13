package com.microservice.store.infrastructure.repositories;

import com.microservice.store.infrastructure.entities.StoreSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreSettingsJpaRepository extends JpaRepository<StoreSettingsEntity, Long> {
    Optional<StoreSettingsEntity> findByStoreId(Long storeId);
    void deleteByStoreId(Long storeId);
}
