package com.microservice.marketing.infrastructure.controller;

import com.microservice.marketing.application.dto.CouponDTO;
import com.microservice.marketing.application.dto.CouponUsageDTO;
import com.microservice.marketing.application.usecase.ManageCouponUseCase;
import com.microservice.marketing.domain.model.Coupon;
import com.microservice.marketing.infrastructure.adapter.mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
@Tag(name = "Coupons", description = "Operations related to coupons")
public class CouponController {

    private final ManageCouponUseCase manageCouponUseCase;
    private final DtoMapper dtoMapper;

    public CouponController(ManageCouponUseCase manageCouponUseCase, DtoMapper dtoMapper) {
        this.manageCouponUseCase = manageCouponUseCase;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new coupon")
    public ResponseEntity<CouponDTO> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(dtoMapper.toDto(manageCouponUseCase.createCoupon(coupon)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID")
    public ResponseEntity<CouponDTO> getCoupon(@PathVariable UUID id) {
        return ResponseEntity.ok(dtoMapper.toDto(manageCouponUseCase.getCoupon(id)));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get coupon by code")
    public ResponseEntity<CouponDTO> getCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(dtoMapper.toDto(manageCouponUseCase.getCouponByCode(code)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete coupon by ID")
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID id) {
        manageCouponUseCase.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/use")
    @Operation(summary = "Use a coupon")
    public ResponseEntity<CouponUsageDTO> useCoupon(@RequestParam String code,
            @RequestParam String userId,
            @RequestParam String orderId,
            @RequestParam BigDecimal discountAmount) {
        return ResponseEntity.ok(dtoMapper.toDto(manageCouponUseCase.useCoupon(code, userId, orderId, discountAmount)));
    }
}
