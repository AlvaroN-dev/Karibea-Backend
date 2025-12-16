package com.microservice.catalog.infrastructure.repositories;

import com.microservice.catalog.infrastructure.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for ProductEntity.
 */
@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findBySkuAndExternalStoreId(String sku, UUID externalStoreId);

    boolean existsBySkuAndExternalStoreId(String sku, UUID externalStoreId);

    List<ProductEntity> findByExternalStoreId(UUID externalStoreId);

    @Query("SELECT p FROM ProductEntity p LEFT JOIN FETCH p.variants LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithDetails(@Param("id") UUID id);
}
