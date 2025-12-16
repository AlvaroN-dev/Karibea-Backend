package com.microservice.inventory.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory.domain.exceptions.StockNotFoundException;
import com.microservice.inventory.domain.models.Stock;
import com.microservice.inventory.domain.port.in.AdjustStockUseCase;
import com.microservice.inventory.domain.port.out.EventPublisherPort;
import com.microservice.inventory.domain.port.out.StockRepositoryPort;

@Service
@Transactional
public class AdjustStockService implements AdjustStockUseCase {

    private final StockRepositoryPort stockRepository;
    private final EventPublisherPort eventPublisher;

    public AdjustStockService(StockRepositoryPort stockRepository, EventPublisherPort eventPublisher) {
        this.stockRepository = stockRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Stock execute(AdjustStockCommand command) {
        Stock stock = stockRepository.findById(command.stockId())
                .orElseThrow(() -> new StockNotFoundException(command.stockId()));

        if (command.movementType().increasesStock()) {
            stock.increaseStock(
                    command.quantity(),
                    command.movementType(),
                    command.referenceType(),
                    command.externalReferenceId(),
                    command.performedById(),
                    command.note());
        } else {
            stock.decreaseStock(
                    command.quantity(),
                    command.movementType(),
                    command.referenceType(),
                    command.externalReferenceId(),
                    command.performedById(),
                    command.note());
        }

        Stock saved = stockRepository.save(stock);
        eventPublisher.publishAll(saved.getDomainEvents());
        saved.clearDomainEvents();

        return saved;
    }
}
