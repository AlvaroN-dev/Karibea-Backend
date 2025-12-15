package com.microservice.inventory.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory.domain.exceptions.StockNotFoundException;
import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.port.in.GetStockAvailabilityUseCase;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;

@Service
@Transactional(readOnly = true)
public class GetStockAvailabilityService implements GetStockAvailabilityUseCase {

    private final StockRepositoryPort stockRepository;

    public GetStockAvailabilityService(StockRepositoryPort stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Stock getById(UUID stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockId));
    }

    @Override
    public Stock getByVariantAndWarehouse(UUID externalVariantId, UUID warehouseId) {
        return stockRepository.findByVariantAndWarehouse(externalVariantId, warehouseId)
                .orElseThrow(() -> new StockNotFoundException(externalVariantId, warehouseId));
    }

    @Override
    public List<Stock> getByVariant(UUID externalVariantId) {
        return stockRepository.findByExternalVariantId(externalVariantId);
    }

    @Override
    public List<Stock> getByWarehouse(UUID warehouseId) {
        return stockRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public List<Stock> getLowStockItems(UUID warehouseId) {
        return stockRepository.findLowStockByWarehouse(warehouseId);
    }
}
