package com.microservice.marketing;

import com.microservice.marketing.application.usecase.ManageCouponUseCase;
import com.microservice.marketing.application.usecase.ManageFlashSaleUseCase;
import com.microservice.marketing.application.usecase.ManagePromotionUseCase;
import com.microservice.marketing.domain.model.Coupon;
import com.microservice.marketing.domain.model.CouponUsage;
import com.microservice.marketing.domain.model.FlashSale;
import com.microservice.marketing.domain.model.Promotion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@org.springframework.kafka.test.context.EmbeddedKafka(partitions = 1, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class MarketingServiceIntegrationTest {

    @Autowired
    private ManagePromotionUseCase managePromotionUseCase;

    @Autowired
    private ManageCouponUseCase manageCouponUseCase;

    @Autowired
    private ManageFlashSaleUseCase manageFlashSaleUseCase;

    @Mock
    private com.microservice.marketing.infrastructure.kafka.KafkaEventProducer kafkaEventProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPromotionLifecycle() {
        Promotion promotion = new Promotion();
        promotion.setName("Summer Sale");
        promotion.setDiscountValue(new BigDecimal("10.00"));
        promotion.setIsActive(true);

        Promotion saved = managePromotionUseCase.createPromotion(promotion);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Summer Sale", saved.getName());

        // Verify Kafka Event Published
        org.mockito.Mockito.verify(kafkaEventProducer, org.mockito.Mockito.times(1))
                .publish(org.mockito.ArgumentMatchers.eq("marketing.promotion.created"),
                        org.mockito.ArgumentMatchers.any());

        Promotion found = managePromotionUseCase.getPromotion(saved.getId());
        Assertions.assertEquals(saved.getId(), found.getId());
    }

    @Test
    void testCouponUsageLifecycle() {
        // 1. Create Promotion
        Promotion promotion = new Promotion();
        promotion.setName("Coupon Promo");
        promotion.setIsActive(true);
        Promotion savedPromo = managePromotionUseCase.createPromotion(promotion);

        // 2. Create Coupon
        Coupon coupon = new Coupon();
        coupon.setPromotionId(savedPromo.getId());
        coupon.setCode("SAVE20");
        coupon.setUsageLimit(10);
        coupon.setUsageCount(0);
        coupon.setIsActive(true);
        Coupon savedCoupon = manageCouponUseCase.createCoupon(coupon);

        Assertions.assertNotNull(savedCoupon.getId());

        // 3. Use Coupon
        CouponUsage usage = manageCouponUseCase.useCoupon("SAVE20", "user123", "order456", new BigDecimal("5.00"));
        Assertions.assertNotNull(usage.getId());
        Assertions.assertEquals("user123", usage.getExternalUserProfileId());

        // Verify Kafka Event Published
        org.mockito.Mockito.verify(kafkaEventProducer, org.mockito.Mockito.times(1))
                .publish(org.mockito.ArgumentMatchers.eq("marketing.coupon.used"), org.mockito.ArgumentMatchers.any());

        // 4. Verify Limit count increased
        Coupon updatedCoupon = manageCouponUseCase.getCoupon(savedCoupon.getId());
        Assertions.assertEquals(1, updatedCoupon.getUsageCount());
    }

    @Test
    void testFlashSaleLifecycle() {
        FlashSale flashSale = new FlashSale();
        flashSale.setName("Flash Sale 1");
        flashSale.setStartedAt(LocalDateTime.now());
        flashSale.setIsActive(true);

        FlashSale saved = manageFlashSaleUseCase.createFlashSale(flashSale);
        Assertions.assertNotNull(saved.getId());

        FlashSale found = manageFlashSaleUseCase.getFlashSale(saved.getId());
        Assertions.assertEquals("Flash Sale 1", found.getName());
    }
}
