package com.microservice.store.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "store_settings")
public class StoreSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_setting")
    private Long id;

    @Column(name = "id_store", unique = true)
    private Long storeId;

    private String returnPolicy;
    private String shippingPolicy;
    private BigDecimal minOrderAmount;
    private boolean acceptsReturns;
    private Integer returnWindowDays;
    // getters / setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getStoreId() {
        return storeId;
    }
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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
