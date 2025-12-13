package com.microservice.store.infrastructure.repositories;

import com.microservice.store.infrastructure.entities.StoreAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreAddressJpaRepository extends JpaRepository<StoreAddressEntity, Long> {
    List<StoreAddressEntity> findByStoreId(Long storeId);
}
