package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.domain.model.Promotion;
import com.microservice.marketing.domain.port.PromotionRepositoryPort;
import com.microservice.marketing.infrastructure.adapter.mapper.PromotionEntityMapper;
import com.microservice.marketing.infrastructure.entities.PromotionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PromotionRepositoryAdapter implements PromotionRepositoryPort {

    private final JpaPromotionRepository jpaPromotionRepository;
    private final PromotionEntityMapper mapper;

    public PromotionRepositoryAdapter(JpaPromotionRepository jpaPromotionRepository, PromotionEntityMapper mapper) {
        this.jpaPromotionRepository = jpaPromotionRepository;
        this.mapper = mapper;
    }

    @Override
    public Promotion save(Promotion promotion) {
        PromotionEntity entity = mapper.toEntity(promotion);
        PromotionEntity saved = jpaPromotionRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Promotion> findById(UUID id) {
        return jpaPromotionRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Promotion> findAll() {
        return jpaPromotionRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaPromotionRepository.deleteById(id);
    }
}
