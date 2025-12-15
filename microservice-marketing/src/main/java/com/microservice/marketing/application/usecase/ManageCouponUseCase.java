package com.microservice.marketing.application.usecase;

import com.microservice.marketing.domain.model.Coupon;
import com.microservice.marketing.domain.model.CouponUsage;
import com.microservice.marketing.domain.port.CouponRepositoryPort;
import com.microservice.marketing.domain.port.CouponUsageRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ManageCouponUseCase {

    private final CouponRepositoryPort couponRepository;
    private final CouponUsageRepositoryPort couponUsageRepository;
    private final com.microservice.marketing.infrastructure.kafka.KafkaEventProducer kafkaEventProducer;

    public ManageCouponUseCase(CouponRepositoryPort couponRepository,
            CouponUsageRepositoryPort couponUsageRepository,
            com.microservice.marketing.infrastructure.kafka.KafkaEventProducer kafkaEventProducer) {
        this.couponRepository = couponRepository;
        this.couponUsageRepository = couponUsageRepository;
        this.kafkaEventProducer = kafkaEventProducer;
    }

    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        if (coupon.getCode() == null || coupon.getCode().isEmpty()) {
            coupon.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    public Coupon getCoupon(UUID id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Coupon not found with code: " + code));
    }

    @Transactional
    public void deleteCoupon(UUID id) {
        couponRepository.deleteById(id);
    }

    @Transactional
    public CouponUsage useCoupon(String code, String userId, String orderId, java.math.BigDecimal discountAmount) {
        Coupon coupon = getCouponByCode(code);

        if (!Boolean.TRUE.equals(coupon.getIsActive())) {
            throw new RuntimeException("Coupon is not active");
        }
        if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
            throw new RuntimeException("Coupon usage limit reached");
        }
        // Additional checks like per user limit and expiry date can be added here

        coupon.setUsageCount(coupon.getUsageCount() == null ? 1 : coupon.getUsageCount() + 1);
        couponRepository.save(coupon);

        CouponUsage usage = new CouponUsage();
        usage.setCouponId(coupon.getId());
        usage.setExternalUserProfileId(userId);
        usage.setExternalOrderId(orderId);
        usage.setDiscountAmount(discountAmount);
        usage.setUsedAt(LocalDateTime.now());

        CouponUsage savedUsage = couponUsageRepository.save(usage);

        kafkaEventProducer.publish("marketing.coupon.used",
                new com.microservice.marketing.domain.event.CouponUsedEvent(
                        savedUsage.getCouponId(),
                        savedUsage.getExternalUserProfileId(),
                        savedUsage.getExternalOrderId(),
                        savedUsage.getDiscountAmount()));

        return savedUsage;
    }
}
