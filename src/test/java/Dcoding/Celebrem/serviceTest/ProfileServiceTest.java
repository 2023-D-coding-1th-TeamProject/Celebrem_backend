package Dcoding.Celebrem.serviceTest;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.profile.UpdateProfileDto;
import Dcoding.Celebrem.repository.MemberRepository;
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

    @Autowired private ProfileRepository profileRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ProfileService profileService;
    @Autowired private TagRepository tagRepository;

    @DisplayName("updateProfile() : 프로필에서 프로필 사진과 소개글을 수정할 수 있다.")
    @Test
    void updateProfileTest() throws Exception {
        //given
        Profile testProfile = makeProfile("testId", 100L, "test", "test url");

        Tag tag1 = new Tag("스포츠");
        Tag tag2 = new Tag("교육");
        Tag tag3 = new Tag("뷰티");

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);

        List<String> updateTagNames = new ArrayList<>();
        updateTagNames.add("스포츠");
        updateTagNames.add("뷰티");

        String updateProfileUrl = "update url";
        String updateDescription = "바뀐 소개글";
        String updateInstagramId = "instagram_test";

        UpdateProfileDto updateProfileDto = new UpdateProfileDto(updateProfileUrl, updateDescription, updateInstagramId, updateTagNames);

        //when
        profileService.updateProfile(1L, updateProfileDto);

        //then
        Assertions.assertTrue(testProfile.isInstagramIdSame("instagram_test"));
        Assertions.assertTrue(testProfile.isProfileTagsSame(2));
        Assertions.assertTrue(testProfile.isDescriptionSame("바뀐 소개글"));
        Assertions.assertTrue(testProfile.isProfileImageUrlSame("update url"));
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