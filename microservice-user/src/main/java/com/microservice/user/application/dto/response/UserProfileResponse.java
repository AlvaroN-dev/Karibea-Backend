package com.microservice.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for user profile information.
 */
@Schema(description = "User profile information response")
public class UserProfileResponse {

    @Schema(description = "Unique profile ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "External user ID from identity service", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID externalUserId;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's middle name", example = "Michael")
    private String middleName;

    @Schema(description = "User's second last name", example = "García")
    private String secondLastname;

    @Schema(description = "Full formatted name", example = "John Michael Doe García")
    private String fullName;

    @Schema(description = "User's phone number", example = "+1-555-123-4567")
    private String phone;

    @Schema(description = "User's gender information")
    private GenderResponse gender;

    @Schema(description = "URL to the user's avatar image", example = "https://example.com/avatars/johndoe.jpg")
    private String avatarUrl;

    @Schema(description = "User's date of birth", example = "1990-05-15")
    private LocalDate dateOfBirth;

    @Schema(description = "Profile creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Profile last update timestamp", example = "2024-06-20T14:45:00")
    private LocalDateTime updatedAt;

    // Constructors
    public UserProfileResponse() {
    }

    public UserProfileResponse(UUID id, UUID externalUserId, String firstName, String lastName,
                               String middleName, String secondLastname, String fullName,
                               String phone, GenderResponse gender, String avatarUrl,
                               LocalDate dateOfBirth, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.externalUserId = externalUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.secondLastname = secondLastname;
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID externalUserId;
        private String firstName;
        private String lastName;
        private String middleName;
        private String secondLastname;
        private String fullName;
        private String phone;
        private GenderResponse gender;
        private String avatarUrl;
        private LocalDate dateOfBirth;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder externalUserId(UUID externalUserId) { this.externalUserId = externalUserId; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder middleName(String middleName) { this.middleName = middleName; return this; }
        public Builder secondLastname(String secondLastname) { this.secondLastname = secondLastname; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder gender(GenderResponse gender) { this.gender = gender; return this; }
        public Builder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
        public Builder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public UserProfileResponse build() {
            return new UserProfileResponse(id, externalUserId, firstName, lastName, middleName,
                secondLastname, fullName, phone, gender, avatarUrl, dateOfBirth, createdAt, updatedAt);
        }
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getExternalUserId() { return externalUserId; }
    public void setExternalUserId(UUID externalUserId) { this.externalUserId = externalUserId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getSecondLastname() { return secondLastname; }
    public void setSecondLastname(String secondLastname) { this.secondLastname = secondLastname; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public GenderResponse getGender() { return gender; }
    public void setGender(GenderResponse gender) { this.gender = gender; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
