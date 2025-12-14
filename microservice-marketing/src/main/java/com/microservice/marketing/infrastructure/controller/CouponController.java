package com.microservice.marketing.infrastructure.controller;

import com.microservice.marketing.application.usecase.ManageCouponUseCase;
import com.microservice.marketing.domain.model.Coupon;
import com.microservice.marketing.domain.model.CouponUsage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final ManageCouponUseCase manageCouponUseCase;

    public CouponController(ManageCouponUseCase manageCouponUseCase) {
        this.manageCouponUseCase = manageCouponUseCase;
    }

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(manageCouponUseCase.createCoupon(coupon));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable Long id) {
        return ResponseEntity.ok(manageCouponUseCase.getCoupon(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Coupon> getCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(manageCouponUseCase.getCouponByCode(code));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        manageCouponUseCase.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/use")
    public ResponseEntity<CouponUsage> useCoupon(@RequestParam String code,
            @RequestParam String userId,
            @RequestParam String orderId,
            @RequestParam BigDecimal discountAmount) {
        return ResponseEntity.ok(manageCouponUseCase.useCoupon(code, userId, orderId, discountAmount));
    }
}
