package com.microservice.shopcart.infrastructure.repositories;

import com.microservice.shopcart.infrastructure.entities.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for ShoppingCartEntity.
 */
@Repository
public interface JpaShoppingCartRepository extends JpaRepository<ShoppingCartEntity, UUID> {

    @Query("SELECT c FROM ShoppingCartEntity c " +
           "LEFT JOIN FETCH c.status " +
           "LEFT JOIN FETCH c.items " +
           "LEFT JOIN FETCH c.coupons " +
           "WHERE c.id = :id AND c.isDeleted = false")
    Optional<ShoppingCartEntity> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT c FROM ShoppingCartEntity c " +
           "WHERE c.externalUserProfilesId = :userId " +
           "AND c.isDeleted = false " +
           "AND c.status.name = 'Active'")
    Optional<ShoppingCartEntity> findActiveByUserProfileId(@Param("userId") UUID userId);

    @Query("SELECT c FROM ShoppingCartEntity c " +
           "WHERE c.sessionId = :sessionId " +
           "AND c.isDeleted = false " +
           "AND c.status.name = 'Active'")
    Optional<ShoppingCartEntity> findActiveBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT c FROM ShoppingCartEntity c " +
           "WHERE c.isDeleted = false " +
           "AND c.status.name = 'Active' " +
           "AND c.expiredAt < :now")
    List<ShoppingCartEntity> findExpiredCarts(@Param("now") Instant now);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM ShoppingCartEntity c WHERE c.id = :id AND c.isDeleted = false")
    boolean existsByIdAndNotDeleted(@Param("id") UUID id);
}
