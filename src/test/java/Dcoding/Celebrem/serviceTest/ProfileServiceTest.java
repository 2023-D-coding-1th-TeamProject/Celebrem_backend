package Dcoding.Celebrem.serviceTest;

import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.profile.UpdateProfileDto;
import Dcoding.Celebrem.repository.ProfileRepository;
import Dcoding.Celebrem.service.ProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProfileServiceTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @DisplayName("updateProfile() : 프로필에서 프로필 사진과 소개글을 수정할 수 있다.")
    @Test
    void updateProfileTest() throws Exception {
        //given
        Profile testProfile = makeProfile("testId", 100L, "test", "test url");

        String updateDescription = "바뀐 소개글";
        String updateProfileUrl = "update url";

        UpdateProfileDto updateProfileDto = new UpdateProfileDto(updateDescription, updateProfileUrl);

        //when
        profileService.updateProfile(1L, updateProfileDto);

        //then
        Profile expectedProfile = profileRepository.findById(1L).get();

        Assertions.assertEquals(expectedProfile, testProfile);
    }

    @DisplayName("findProfile(): id로 프로필을 가져올 수 있다.")
    @Test
    void findProfileTest() throws Exception {
        //given
        Profile testProfile = makeProfile("testId", 100L, "test", "testUrl");

        //when
        Profile ResultProfile = profileService.findById(1L);

        //then
        Assertions.assertEquals(testProfile, ResultProfile);
    }

    private Profile makeProfile(String instagramId, long followerCount, String description, String profileUrl) {
        Profile Profile = new Profile(instagramId, followerCount, description, profileUrl);
        profileRepository.save(Profile);
        return Profile;
    }
}