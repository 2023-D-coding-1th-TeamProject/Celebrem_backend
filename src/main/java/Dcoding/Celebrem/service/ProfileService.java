package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.profile.UpdateProfileDto;
import Dcoding.Celebrem.dto.search.MainSearch;
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
    @Transactional
    public void updateProfile(Long profileId, UpdateProfileDto updateProfileDto){
        Profile profile = findById(profileId);

        profile.changeProfileImage(updateProfileDto.getProfileImageUrl());
        profile.changeProfileDescription(updateProfileDto.getDescription());
    }

    /**
     * 프로필 반환 메소드
     */
    public Profile findById(Long profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if(profile.isPresent()){
            return profile.get();
        }
        return null;
    }

    /**
     * 찜 목록 가져오기 v1 : 프로필 화면에서 이름 검색만
     * 프로필사진, 닉네임, 날짜?, 좋아요 수, 팔로워 수
     * fromId(좋아요를 누른 사람)으로 조회해 ToId(인플루언서) 목록을 가져온다.
     */
    public void findAllByName() {
        //return likesRepository.findAllByName(likesSearch.getName());
    }

    /**
     * 인플루언서 목록 가져오기 v2 : 인플루언서 검색 화면에서  태그+조건 검색 -> 동적쿼리
     * profileService에 구현?
     */
    public void findAllByName(MainSearch mainSearch) {
        //return profileRepository.findAllByName(mainSearch);
    }
}
