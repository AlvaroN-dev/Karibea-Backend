package com.microservice.marketing.domain.event;

import java.math.BigDecimal;

public class CouponUsedEvent extends DomainEvent {
    private final Long couponId;
    private final String externalUserProfileId;
    private final String externalOrderId;
    private final BigDecimal discountAmount;

    public CouponUsedEvent(Long couponId, String externalUserProfileId, String externalOrderId,
            BigDecimal discountAmount) {
        super("CouponUsedEvent");
        this.couponId = couponId;
        this.externalUserProfileId = externalUserProfileId;
        this.externalOrderId = externalOrderId;
        this.discountAmount = discountAmount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
}
