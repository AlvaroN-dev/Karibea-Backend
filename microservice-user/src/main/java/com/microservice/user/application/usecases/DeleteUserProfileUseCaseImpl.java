package com.microservice.user.application.usecases;

import com.microservice.user.domain.events.UserProfileDeletedEvent;
import com.microservice.user.domain.exceptions.UserProfileNotFoundException;
import com.microservice.user.domain.models.UserProfile;
import com.microservice.user.domain.port.in.DeleteUserProfileUseCase;
import com.microservice.user.domain.port.out.EventPublisherPort;
import com.microservice.user.domain.port.out.UserProfileRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementación del caso de uso para eliminar perfil de usuario (soft delete)
 */
@Service
@Transactional
public class DeleteUserProfileUseCaseImpl implements DeleteUserProfileUseCase {
    
    private final UserProfileRepositoryPort userProfileRepository;
    private final EventPublisherPort eventPublisher;
    
    public DeleteUserProfileUseCaseImpl(UserProfileRepositoryPort userProfileRepository,
                                        EventPublisherPort eventPublisher) {
        this.userProfileRepository = userProfileRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public void execute(UUID profileId) {
        UserProfile profile = userProfileRepository.findById(profileId)
            .orElseThrow(() -> new UserProfileNotFoundException(profileId));
        
        profile.markAsDeleted();
        userProfileRepository.save(profile);
        
        eventPublisher.publish(UserProfileDeletedEvent.create(
            profile.getId(),
            profile.getExternalUserId()
        ));
    }
    
    @Override
    public void executeByExternalUserId(UUID externalUserId) {
        UserProfile profile = userProfileRepository.findByExternalUserId(externalUserId)
            .orElseThrow(() -> UserProfileNotFoundException.byExternalUserId(externalUserId));
        
        profile.markAsDeleted();
        userProfileRepository.save(profile);
        
        eventPublisher.publish(UserProfileDeletedEvent.create(
            profile.getId(),
            profile.getExternalUserId()
        ));
    }
}
