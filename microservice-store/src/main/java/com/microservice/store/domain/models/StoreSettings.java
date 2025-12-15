package com.microservice.store.domain.models;

import java.math.BigDecimal;
import java.util.UUID;

public class StoreSettings {
    private UUID id;
    private String returnPolicy;
    private String shippingPolicy;
    private BigDecimal minOrderAmount;
    private boolean acceptsReturns;
    private Integer returnWindowDays;
    // getters and setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getReturnPolicy() {
        return returnPolicy;
    }
    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
    }
    public String getShippingPolicy() {
        return shippingPolicy;
    }
    public void setShippingPolicy(String shippingPolicy) {
        this.shippingPolicy = shippingPolicy;
    }
    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }
    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }
    public boolean isAcceptsReturns() {
        return acceptsReturns;
    }
    public void setAcceptsReturns(boolean acceptsReturns) {
        this.acceptsReturns = acceptsReturns;
    }
    public Integer getReturnWindowDays() {
        return returnWindowDays;
    }
    public void setReturnWindowDays(Integer returnWindowDays) {
        this.returnWindowDays = returnWindowDays;
    }
    
}
