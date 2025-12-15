package com.microservice.inventory.infrastructure.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;
import com.microservice.inventory.infrastructure.adapters.mapper.StockEntityMapper;
import com.microservice.inventory.infrastructure.entities.StockEntity;
import com.microservice.inventory.infrastructure.repositories.JpaStockRepository;

@Component
public class StockRepositoryAdapter implements StockRepositoryPort {

    private final JpaStockRepository jpaRepository;
    private final StockEntityMapper mapper;

    public StockRepositoryAdapter(JpaStockRepository jpaRepository, StockEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Stock save(Stock stock) {
        StockEntity entity = mapper.toEntity(stock);
        StockEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Stock> findById(UUID stockId) {
        return jpaRepository.findById(stockId).map(mapper::toDomain);
    }

    @Override
    public Optional<Stock> findByVariantAndWarehouse(UUID externalVariantId, UUID warehouseId) {
        return jpaRepository.findByVariantAndWarehouse(externalVariantId, warehouseId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Stock> findByExternalVariantId(UUID externalVariantId) {
        return jpaRepository.findByExternalVariantId(externalVariantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Stock> findByWarehouseId(UUID warehouseId) {
        return jpaRepository.findByWarehouseId(warehouseId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Stock> findLowStockByWarehouse(UUID warehouseId) {
        return jpaRepository.findLowStockByWarehouse(warehouseId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByVariantAndWarehouse(UUID externalVariantId, UUID warehouseId) {
        return jpaRepository.existsByExternalVariantIdAndWarehouseId(externalVariantId, warehouseId);
    }
}
