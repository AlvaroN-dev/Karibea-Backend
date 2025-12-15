package com.microservice.store.infrastructure.repositories;

import com.microservice.store.infrastructure.entities.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, UUID> {

    Optional<StoreEntity> findByExternalOwnerUserId(UUID externalOwnerUserId);

    List<StoreEntity> findByDeletedFalse();
}
