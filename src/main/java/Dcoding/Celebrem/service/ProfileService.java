package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.profile.UpdateProfileDto;
import Dcoding.Celebrem.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    /**
     *  프로필 업데이트 메소드
     */
    public void updateProfile(Long profileId, UpdateProfileDto updateProfileDto){
        Profile profile = findById(profileId);

        profile.changeProfileImage(updateProfileDto.getProfileImageUrl());
        profile.changeProfileDescription(updateProfileDto.getDescription());
    }

    public Profile findById(Long profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if(profile.isPresent()){
            return profile.get();
        }
        return null;
    }
}
