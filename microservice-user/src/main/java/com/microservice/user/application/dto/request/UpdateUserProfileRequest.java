package com.microservice.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for updating a user profile.
 * All fields are optional - only provided fields will be updated.
 */
@Schema(description = "Request to update an existing user profile")
public class UpdateUserProfileRequest {

    @Schema(
        description = "User's first name",
        example = "John",
        maxLength = 255
    )
    @Size(max = 255, message = "First name must be less than 255 characters")
    private String firstName;

    @Schema(
        description = "User's last name",
        example = "Doe",
        maxLength = 255
    )
    @Size(max = 255, message = "Last name must be less than 255 characters")
    private String lastName;

    @Schema(
        description = "User's middle name",
        example = "Michael",
        maxLength = 255
    )
    @Size(max = 255, message = "Middle name must be less than 255 characters")
    private String middleName;

    @Schema(
        description = "User's second last name",
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
    public UpdateUserProfileRequest() {
    }

    public UpdateUserProfileRequest(String firstName, String lastName, String middleName,
                                    String secondLastname, String phone, UUID genderId,
                                    String avatarUrl, LocalDate dateOfBirth) {
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
