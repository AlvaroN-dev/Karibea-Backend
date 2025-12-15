package com.microservice.user.infrastructure.repositories;

import com.microservice.user.infrastructure.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para direcciones
 */
@Repository
public interface JpaAddressRepository extends JpaRepository<AddressEntity, UUID> {
    
    List<AddressEntity> findByExternalUserId(UUID externalUserId);
    
    List<AddressEntity> findByExternalUserIdAndDeletedFalse(UUID externalUserId);
    
    Optional<AddressEntity> findByExternalUserIdAndIsDefaultTrueAndDeletedFalse(UUID externalUserId);
    
    @Modifying
    @Query("UPDATE AddressEntity a SET a.isDefault = false WHERE a.externalUserId = :externalUserId AND a.isDefault = true")
    void removeDefaultByExternalUserId(@Param("externalUserId") UUID externalUserId);
}
