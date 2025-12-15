package com.microservice.marketing.infrastructure.repositories;

import com.microservice.marketing.infrastructure.entities.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.UUID;

@Repository
public interface JpaCouponRepository extends JpaRepository<CouponEntity, UUID> {
    Optional<CouponEntity> findByCode(String code);
}
