package com.microservice.store.domain.models;
import java.util.UUID;

public class StoreStatus {
    private UUID id;
    private String name;
    private String verificationStatus;
    private String description;
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVerificationStatus() {
        return verificationStatus;
    }
    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}
