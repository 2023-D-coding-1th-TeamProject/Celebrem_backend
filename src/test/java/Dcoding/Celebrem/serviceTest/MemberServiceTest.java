package Dcoding.Celebrem.serviceTest;

import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import Dcoding.Celebrem.repository.TagRepository;
import Dcoding.Celebrem.service.MemberService;
import Dcoding.Celebrem.service.ProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired private ProfileService profileService;
    @Autowired private TagRepository tagRepository;

    @DisplayName("findProfile(): id로 프로필을 가져올 수 있다.")
    @Test
    void findMemberByIdTest() throws Exception {
        //given
        Profile profile = makeProfile("testId", 100L, "test", "testUrl");
        Member testMember = makeMember("abc@abc", "010-010", "password", "nickname", profile);

        //when
        Member resultMember = memberService.findMemberById(1L);

        //then
        Assertions.assertEquals(testMember, resultMember);
    }

    @DisplayName("닉네임_기반_검색(): 닉네임 기반으로 인플루언서 권한을 지닌 사용자를 검색할 수 있다.")
    @Test
    void findAllByAuthorityAndNicknameTest(){
        //given
        Profile profile1 = makeProfile("testId", 100L, "test", "testUrl");
        Member testMember1 = makeMember("abc@abc", "010-010", "password", "nickname", profile1);

        Profile profile2 = makeProfile("testId2", 100L, "test2", "testUrl2");
        Member testMember2 = makeMember("abc@abc2", "010-0102", "password2", "nickname2", profile2);

        testMember1.checkAuthorityToInfluencer();
        Pageable pageable = PageRequest.of(0,20);

        //when
        Page<Member> resultMembers = memberRepository.findAllByAuthorityAndNicknameContaining(Authority.ROLE_INFLUENCER, "ckna", pageable);
        Member resultMember = resultMembers.getContent().get(0);

        //then
        Assertions.assertEquals(resultMember.authorityToString(), "ROLE_INFLUENCER");
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
