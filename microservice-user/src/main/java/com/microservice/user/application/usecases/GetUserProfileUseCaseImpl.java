package com.microservice.user.application.usecases;

import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.domain.port.in.GetUserProfileUseCase;
import com.microservice.user.domain.port.out.UserProfileRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del caso de uso para consultar perfiles de usuario
 */
@Service
@Transactional(readOnly = true)
public class GetUserProfileUseCaseImpl implements GetUserProfileUseCase {
    
    private final UserProfileRepositoryPort userProfileRepository;
    
    public GetUserProfileUseCaseImpl(UserProfileRepositoryPort userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
    
    @Override
    public Optional<UserProfile> findById(UUID profileId) {
        return userProfileRepository.findById(profileId);
    }
    
    @Override
    public Optional<UserProfile> findByExternalUserId(UUID externalUserId) {
        return userProfileRepository.findByExternalUserId(externalUserId);
    }
    
    @Override
    public Page<UserProfile> findAll(Pageable pageable) {
        return userProfileRepository.findAllActive(pageable);
    }
    
    @Override
    public boolean existsByExternalUserId(UUID externalUserId) {
        return userProfileRepository.existsByExternalUserId(externalUserId);
    }
}
