package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.domain.model.Coupon;
import com.microservice.marketing.domain.port.CouponRepositoryPort;
import com.microservice.marketing.infrastructure.adapter.mapper.CouponEntityMapper;
import com.microservice.marketing.infrastructure.entities.CouponEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CouponRepositoryAdapter implements CouponRepositoryPort {

    private final JpaCouponRepository jpaCouponRepository;
    private final CouponEntityMapper mapper;

    public CouponRepositoryAdapter(JpaCouponRepository jpaCouponRepository, CouponEntityMapper mapper) {
        this.jpaCouponRepository = jpaCouponRepository;
        this.mapper = mapper;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity = mapper.toEntity(coupon);
        CouponEntity saved = jpaCouponRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return jpaCouponRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return jpaCouponRepository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public List<Coupon> findAll() {
        return jpaCouponRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaCouponRepository.deleteById(id);
    }
}
