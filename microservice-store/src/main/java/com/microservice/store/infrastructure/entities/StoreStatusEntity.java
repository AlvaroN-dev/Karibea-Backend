package com.microservice.store.infrastructure.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stores_status")
public class StoreStatusEntity {

    @Id
    private Long id;

    private String name;

    @Column(name = "verification_status")
    private String verificationStatus;

    private String description;
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
