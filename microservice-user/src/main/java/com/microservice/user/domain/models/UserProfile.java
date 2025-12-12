package com.microservice.user.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root - Perfil de usuario
 * Representa la información personal del usuario en el bounded context Profile
 */
public class UserProfile {
    
    private final UUID id;
    private final UUID externalUserId; // Referencia al user.id en identity_db
    private String firstName;
    private String lastName;
    private String middleName;
    private String secondLastname;
    private String phone;
    private Gender gender;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    
    // Constructor privado - usar factory methods
    private UserProfile(UUID id, UUID externalUserId, String firstName, String lastName,
                        String middleName, String secondLastname, String phone,
                        Gender gender, String avatarUrl, LocalDate dateOfBirth,
                        LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        this.id = id;
        this.externalUserId = externalUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.secondLastname = secondLastname;
        this.phone = phone;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }
    
    /**
     * Factory method para crear un nuevo perfil de usuario
     */
    public static UserProfile create(UUID externalUserId, String firstName, String lastName) {
        Objects.requireNonNull(externalUserId, "External user ID cannot be null");
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }
        
        LocalDateTime now = LocalDateTime.now();
        return new UserProfile(
            UUID.randomUUID(),
            externalUserId,
            firstName.trim(),
            lastName.trim(),
            null,
            null,
            null,
            null,
            null,
            null,
            now,
            now,
            false
        );
    }
    
    /**
     * Factory method para reconstruir desde persistencia
     */
    public static UserProfile reconstitute(UUID id, UUID externalUserId, String firstName,
                                           String lastName, String middleName, String secondLastname,
                                           String phone, Gender gender, String avatarUrl,
                                           LocalDate dateOfBirth, LocalDateTime createdAt,
                                           LocalDateTime updatedAt, boolean deleted) {
        return new UserProfile(id, externalUserId, firstName, lastName, middleName,
                              secondLastname, phone, gender, avatarUrl, dateOfBirth,
                              createdAt, updatedAt, deleted);
    }
    
    // Métodos de dominio
    
    public void updatePersonalInfo(String firstName, String lastName, String middleName,
                                   String secondLastname, LocalDate dateOfBirth) {
        if (firstName != null && !firstName.isBlank()) {
            this.firstName = firstName.trim();
        }
        if (lastName != null && !lastName.isBlank()) {
            this.lastName = lastName.trim();
        }
        this.middleName = middleName != null ? middleName.trim() : null;
        this.secondLastname = secondLastname != null ? secondLastname.trim() : null;
        this.dateOfBirth = dateOfBirth;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateContactInfo(String phone) {
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void assignGender(Gender gender) {
        this.gender = gender;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsDeleted() {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        if (middleName != null && !middleName.isBlank()) {
            sb.append(" ").append(middleName);
        }
        sb.append(" ").append(lastName);
        if (secondLastname != null && !secondLastname.isBlank()) {
            sb.append(" ").append(secondLastname);
        }
        return sb.toString();
    }
    
    // Getters
    
    public UUID getId() {
        return id;
    }
    
    public UUID getExternalUserId() {
        return externalUserId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public String getSecondLastname() {
        return secondLastname;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
