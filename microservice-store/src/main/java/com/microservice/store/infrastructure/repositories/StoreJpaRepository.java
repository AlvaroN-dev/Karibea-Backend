package com.microservice.store.infrastructure.repositories;

import com.microservice.store.infrastructure.entities.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByExternalOwnerUserId(String externalOwnerUserId);

    List<StoreEntity> findByDeletedFalse();
}
