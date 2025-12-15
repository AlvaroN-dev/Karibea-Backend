package com.microservice.identity.infrastructure.adapters;

import com.microservice.identity.domain.models.User;
import com.microservice.identity.domain.port.out.UserRepositoryPort;
import com.microservice.identity.infrastructure.entities.UserEntity;
import com.microservice.identity.infrastructure.repositories.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA Adapter for UserRepositoryPort.
 * Implements the repository port using JPA and entity-domain model conversion.
 * Follows hexagonal architecture by adapting infrastructure to domain.
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(UserEntity::toDomainModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(UserEntity::toDomainModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserEntity::toDomainModel);
    }

    @Override
    public Optional<User> findByVerificationToken(String token) {
        return userJpaRepository.findByVerificationToken(token)
                .map(UserEntity::toDomainModel);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(UserEntity::toDomainModel)
                .toList();
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.fromDomainModel(user);
        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return savedEntity.toDomainModel();
    }

    @Override
    public void delete(User user) {
        UserEntity userEntity = UserEntity.fromDomainModel(user);
        userJpaRepository.delete(userEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
