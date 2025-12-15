package com.microservice.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for creating a user profile.
 * Contains personal information to create a new user profile linked to an identity user.
 */
@Schema(description = "Request to create a new user profile")
public class CreateUserProfileRequest {

    @Schema(
        description = "External user ID from the identity service (must match authenticated user)",
        example = "550e8400-e29b-41d4-a716-446655440000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "External user ID is required")
    private UUID externalUserId;

    @Schema(
        description = "User's first name",
        example = "John",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 1,
        maxLength = 255
    )
    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must be less than 255 characters")
    private String firstName;

    @Schema(
        description = "User's last name",
        example = "Doe",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 1,
        maxLength = 255
    )
    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must be less than 255 characters")
    private String lastName;

    @Schema(
        description = "User's middle name (optional)",
        example = "Michael",
        maxLength = 255
    )
    @Size(max = 255, message = "Middle name must be less than 255 characters")
    private String middleName;

    @Schema(
        description = "User's second last name (optional, common in Hispanic cultures)",
        example = "García",
        maxLength = 255
    )
    @Size(max = 255, message = "Second lastname must be less than 255 characters")
    private String secondLastname;

    @Schema(
        description = "User's phone number",
        example = "+1-555-123-4567",
        maxLength = 100
    )
    @Size(max = 100, message = "Phone must be less than 100 characters")
    private String phone;

    @Schema(
        description = "Gender ID from the catalog",
        example = "550e8400-e29b-41d4-a716-446655440001"
    )
    private UUID genderId;

    @Schema(
        description = "URL to the user's avatar image",
        example = "https://example.com/avatars/johndoe.jpg"
    )
    private String avatarUrl;

    @Schema(
        description = "User's date of birth",
        example = "1990-05-15"
    )
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // Constructors
    public CreateUserProfileRequest() {
    }

    public CreateUserProfileRequest(UUID externalUserId, String firstName, String lastName,
                                    String middleName, String secondLastname, String phone,
                                    UUID genderId, String avatarUrl, LocalDate dateOfBirth) {
        this.externalUserId = externalUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.secondLastname = secondLastname;
        this.phone = phone;
        this.genderId = genderId;
        this.avatarUrl = avatarUrl;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public UUID getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(UUID externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSecondLastname() {
        return secondLastname;
    }

    public void setSecondLastname(String secondLastname) {
        this.secondLastname = secondLastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UUID getGenderId() {
        return genderId;
    }

    public void setGenderId(UUID genderId) {
        this.genderId = genderId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
