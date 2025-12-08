package com.microservice.identity.domain.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String email;
    private String passwordHash;
    private boolean enabled;
    private boolean emailVerified;
    private boolean isVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean isDeleted;
    private String verificationToken;
    private Set<Role> roles = new HashSet<>();

    private User() {
    }

    public static User create(String username, String email, String passwordHash) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.passwordHash = passwordHash;
        user.enabled = true;
        user.emailVerified = false;
        user.isVerified = false;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        user.verificationToken = UUID.randomUUID().toString();
        return user;
    }

    public void verifyEmail() {
        if (this.emailVerified) {
            throw new IllegalStateException("Email already verified");
        }
        this.emailVerified = true;
        this.isVerified = true;
        this.verificationToken = null;
        this.updatedAt = LocalDateTime.now();

    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfile(String newEmail, String newUsername) {
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        this.email = newEmail;

        if (newUsername != null && !newUsername.isBlank()) {
            this.username = newUsername;
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        if (this.isDeleted) {
            throw new IllegalStateException("User is already deleted");
        }

        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.enabled = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePasswordHash(String newPasswordHash) {
        if (this.isDeleted) {
            throw new IllegalStateException("Cannot change password for deleted user");
        }
        this.passwordHash = newPasswordHash;
        this.updatedAt = LocalDateTime.now();
    }

    public void addRole(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeRole(Role role) {
        if (roles != null) {
            roles.remove(role);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean hasRole(String roleName) {
        return roles != null && roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    // Factory method for reconstruction (Infrastructure -> Domain)
    // factory method to access the Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class for infrastructure reconstruction
    public static class Builder {
        private final User user;

        public Builder() {
            this.user = new User();
        }

        public Builder id(UUID id) {
            user.id = id;
            return this;
        }

        public Builder username(String username) {
            user.username = username;
            return this;
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            user.passwordHash = passwordHash;
            return this;
        }

        public Builder enabled(boolean enabled) {
            user.enabled = enabled;
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            user.emailVerified = emailVerified;
            return this;
        }

        public Builder isVerified(boolean isVerified) {
            user.isVerified = isVerified;
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            user.lastLogin = lastLogin;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            user.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            user.updatedAt = updatedAt;
            return this;
        }

        public Builder deletedAt(LocalDateTime deletedAt) {
            user.deletedAt = deletedAt;
            return this;
        }

        public Builder isDeleted(boolean isDeleted) {
            user.isDeleted = isDeleted;
            return this;
        }

        public Builder verificationToken(String verificationToken) {
            user.verificationToken = verificationToken;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            user.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
            return this;
        }

        public User build() {
            return user;
        }
    }

    // Setters protegidos para reconstrucción - Can be removed if reconstruct is
    // used exclusively
    protected void setId(UUID id) {
        this.id = id;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    protected void setVerified(boolean verified) {
        isVerified = verified;
    }

    protected void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    protected void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    protected void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    protected void setRoles(Set<Role> roles) {
        this.roles = new HashSet<>(roles);
    }
}
