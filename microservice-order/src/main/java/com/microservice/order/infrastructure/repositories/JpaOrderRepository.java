package com.microservice.order.infrastructure.repositories;

import com.microservice.order.infrastructure.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for Order entities.
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.coupons WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(@Param("id") UUID id);

    Page<OrderEntity> findByExternalUserProfileId(UUID externalUserProfileId, Pageable pageable);

    Page<OrderEntity> findByExternalStoreId(UUID externalStoreId, Pageable pageable);

    boolean existsByOrderNumber(String orderNumber);

    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status")
    Page<OrderEntity> findByStatus(@Param("status") String status, Pageable pageable);
}
