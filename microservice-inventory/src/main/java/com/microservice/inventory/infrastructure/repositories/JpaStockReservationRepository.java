package com.microservice.inventory.infrastructure.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.inventory.infrastructure.entities.StockReservationEntity;

@Repository
public interface JpaStockReservationRepository extends JpaRepository<StockReservationEntity, UUID> {

    List<StockReservationEntity> findByStockId(UUID stockId);

    List<StockReservationEntity> findByExternalCartId(UUID externalCartId);

    List<StockReservationEntity> findByExternalOrderId(UUID externalOrderId);

    @Query("SELECT r FROM StockReservationEntity r WHERE r.expiresAt < :now AND r.status NOT IN ('EXPIRED', 'CANCELLED', 'COMPLETED')")
    List<StockReservationEntity> findExpiredReservations(@Param("now") LocalDateTime now);

    List<StockReservationEntity> findByStatus(String status);
}
