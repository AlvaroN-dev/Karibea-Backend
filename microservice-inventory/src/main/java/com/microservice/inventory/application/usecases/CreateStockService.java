package com.microservice.inventory.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.port.in.CreateStockUseCase;
import com.microservice.inventory.domain.port.out.EventPublisherPort;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;

@Service
@Transactional
public class CreateStockService implements CreateStockUseCase {

    private final StockRepositoryPort stockRepository;
    private final EventPublisherPort eventPublisher;

    public CreateStockService(StockRepositoryPort stockRepository, EventPublisherPort eventPublisher) {
        this.stockRepository = stockRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Stock execute(CreateStockCommand command) {
        // Check if stock already exists
        if (stockRepository.existsByVariantAndWarehouse(command.externalVariantId(), command.warehouseId())) {
            throw new IllegalArgumentException("Stock already exists for this variant in this warehouse");
        }

        Stock stock = Stock.builder()
                .externalProductId(command.externalProductId())
                .externalVariantId(command.externalVariantId())
                .warehouseId(command.warehouseId())
                .quantityAvailable(command.initialQuantity())
                .lowStockThreshold(command.lowStockThreshold())
                .reorderPoint(command.reorderPoint())
                .build();

        Stock saved = stockRepository.save(stock);
        eventPublisher.publishAll(saved.getDomainEvents());
        saved.clearDomainEvents();

        return saved;
    }
}
