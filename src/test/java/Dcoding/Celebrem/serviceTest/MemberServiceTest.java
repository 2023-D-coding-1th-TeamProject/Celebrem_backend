package Dcoding.Celebrem.serviceTest;

import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import Dcoding.Celebrem.service.MemberService;
import Dcoding.Celebrem.service.ProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MemberService memberService;

    @DisplayName("findProfile(): id로 프로필을 가져올 수 있다.")
    @Test
    void findProfileTest() throws Exception {
        //given
        Profile profile = makeProfile("testId", 100L, "test", "testUrl");
        Member testMember = makeMember("abc@abc", "010-010", "password", "nickname", profile);

        //when
        Member resultMember = memberService.findMemberById(1L);

        //then
        Assertions.assertEquals(testMember, resultMember);
    }

    @Test
    void findByAuthorityAndNickname(){
        Profile profile = makeProfile("testId", 100L, "test", "testUrl");
        Member testMember = makeMember("abc@abc", "010-010", "password", "nickname", profile);

        memberRepository.save(testMember);

        memberRepository.findByAuthorityAndNicknameContaining(Authority.ROLE_USER, "nickname");
    }

    private Member makeMember(String email, String phoneNumber, String password, String nickname, Profile profile) {
        Member member = new Member(email, phoneNumber, password, nickname, profile);
        memberRepository.save(member);
        return member;
    }

    private Profile makeProfile(String instagramId, long followerCount, String description, String profileUrl) {
        Profile Profile = new Profile(instagramId, followerCount, description, profileUrl);
        profileRepository.save(Profile);
        return Profile;
    }
}
