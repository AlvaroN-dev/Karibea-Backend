package com.microservice.search.infrastructure.repositories;

import com.microservice.search.infrastructure.entities.ProductIndexEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para ProductIndexEntity.
 */
@Repository
public interface ProductIndexRepository extends JpaRepository<ProductIndexEntity, UUID> {

    Optional<ProductIndexEntity> findByExternalProductId(UUID externalProductId);

    List<ProductIndexEntity> findByExternalStoreIdAndIsActiveTrueAndIsDeletedFalse(UUID externalStoreId);

    @Query("""
            SELECT p FROM ProductIndexEntity p
            WHERE p.isActive = true
            AND p.isDeleted = false
            AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
                 OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
                 OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :term, '%')))
            ORDER BY p.salesCount DESC NULLS LAST
            """)
    Page<ProductIndexEntity> searchByTerm(@Param("term") String term, Pageable pageable);

    @Query("""
            SELECT p FROM ProductIndexEntity p
            WHERE p.isActive = true
            AND p.isDeleted = false
            """)
    List<ProductIndexEntity> findAllActive();

    boolean existsByExternalProductId(UUID externalProductId);

    long countByExternalStoreIdAndIsActiveTrueAndIsDeletedFalse(UUID externalStoreId);
}
