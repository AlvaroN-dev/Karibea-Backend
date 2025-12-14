package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.domain.model.CouponUsage;
import com.microservice.marketing.domain.port.CouponUsageRepositoryPort;
import com.microservice.marketing.infrastructure.adapter.mapper.CouponUsageEntityMapper;
import com.microservice.marketing.infrastructure.entities.CouponUsageEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CouponUsageRepositoryAdapter implements CouponUsageRepositoryPort {

    private final JpaCouponUsageRepository jpaCouponUsageRepository;
    private final CouponUsageEntityMapper mapper;

    public CouponUsageRepositoryAdapter(JpaCouponUsageRepository jpaCouponUsageRepository,
            CouponUsageEntityMapper mapper) {
        this.jpaCouponUsageRepository = jpaCouponUsageRepository;
        this.mapper = mapper;
    }

    @Override
    public CouponUsage save(CouponUsage couponUsage) {
        CouponUsageEntity entity = mapper.toEntity(couponUsage);
        CouponUsageEntity saved = jpaCouponUsageRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<CouponUsage> findByCouponId(Long couponId) {
        return jpaCouponUsageRepository.findByCouponId(couponId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponUsage> findByExternalUserProfileId(String userId) {
        return jpaCouponUsageRepository.findByExternalUserProfileId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
