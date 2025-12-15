package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.domain.model.FlashSale;
import com.microservice.marketing.domain.port.FlashSaleRepositoryPort;
import com.microservice.marketing.infrastructure.adapter.mapper.FlashSaleEntityMapper;
import com.microservice.marketing.infrastructure.entities.FlashSaleEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FlashSaleRepositoryAdapter implements FlashSaleRepositoryPort {

    private final JpaFlashSaleRepository jpaFlashSaleRepository;
    private final FlashSaleEntityMapper mapper;

    public FlashSaleRepositoryAdapter(JpaFlashSaleRepository jpaFlashSaleRepository, FlashSaleEntityMapper mapper) {
        this.jpaFlashSaleRepository = jpaFlashSaleRepository;
        this.mapper = mapper;
    }

    @Override
    public FlashSale save(FlashSale flashSale) {
        FlashSaleEntity entity = mapper.toEntity(flashSale);
        FlashSaleEntity saved = jpaFlashSaleRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<FlashSale> findById(UUID id) {
        return jpaFlashSaleRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<FlashSale> findAll() {
        return jpaFlashSaleRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaFlashSaleRepository.deleteById(id);
    }
}
