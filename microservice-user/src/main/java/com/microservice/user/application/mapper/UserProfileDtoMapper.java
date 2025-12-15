package com.microservice.user.application.mapper;

import com.microservice.user.application.dto.response.GenderResponse;
import com.microservice.user.application.dto.response.UserProfileResponse;
import com.microservice.user.domain.models.Gender;
import com.microservice.user.domain.models.UserProfile;
import org.springframework.stereotype.Component;

/**
 * Mapper entre dominio y DTOs para perfiles de usuario
 */
@Component
public class UserProfileDtoMapper {
    
    public UserProfileResponse toResponse(UserProfile profile) {
        if (profile == null) {
            return null;
        }
        
        return new UserProfileResponse(
            profile.getId(),
            profile.getExternalUserId(),
            profile.getFirstName(),
            profile.getLastName(),
            profile.getMiddleName(),
            profile.getSecondLastname(),
            profile.getFullName(),
            profile.getPhone(),
            toGenderResponse(profile.getGender()),
            profile.getAvatarUrl(),
            profile.getDateOfBirth(),
            profile.getCreatedAt(),
            profile.getUpdatedAt()
        );
    }
    
    private GenderResponse toGenderResponse(Gender gender) {
        if (gender == null) {
            return null;
        }
        return new GenderResponse(gender.getId(), gender.getName());
    }
}
