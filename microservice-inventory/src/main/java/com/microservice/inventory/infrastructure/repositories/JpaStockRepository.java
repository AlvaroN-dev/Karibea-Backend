package com.microservice.inventory.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.inventory.infrastructure.entities.StockEntity;

@Repository
public interface JpaStockRepository extends JpaRepository<StockEntity, UUID> {

    @Query("SELECT s FROM StockEntity s WHERE s.externalVariantId = :variantId AND s.warehouseId = :warehouseId")
    Optional<StockEntity> findByVariantAndWarehouse(@Param("variantId") UUID variantId,
            @Param("warehouseId") UUID warehouseId);

    List<StockEntity> findByExternalVariantId(UUID externalVariantId);

    List<StockEntity> findByWarehouseId(UUID warehouseId);

    @Query("SELECT s FROM StockEntity s WHERE s.warehouseId = :warehouseId AND s.quantityAvailable <= s.lowStockThreshold")
    List<StockEntity> findLowStockByWarehouse(@Param("warehouseId") UUID warehouseId);

    boolean existsByExternalVariantIdAndWarehouseId(UUID externalVariantId, UUID warehouseId);
}
