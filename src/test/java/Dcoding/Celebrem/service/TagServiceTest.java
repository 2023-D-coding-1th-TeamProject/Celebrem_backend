package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.tag.ProfileTag;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.tag.TagSetupRequestDto;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileTagRepository;
import Dcoding.Celebrem.repository.TagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

@DataJpaTest
class TagServiceTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProfileTagRepository profileTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    private TagService tagService;

    @BeforeEach
    public void setup() {
        tagService = new TagService(tagRepository, profileTagRepository, memberRepository);
    }

    @Test
    @DisplayName("태그 설정")
    @WithMockUser(username = "acg6138@naver.com")
    public void setupTagTest() {
        // given
        Member member = Member.builder().email("acg6138@naver.com").build();
        memberRepository.save(member);

        Tag tag = Tag.builder().name("게임").build();
        tagRepository.save(tag);

        // when
        tagService.setUpProfileTags(new TagSetupRequestDto(List.of("게임")));
        List<ProfileTag> myProfileTags = member.getProfile().getProfileTags();
        ProfileTag profileTag = profileTagRepository.findByTag(tag).get();

        // then
        Assertions.assertThat(myProfileTags.contains(profileTag)).isTrue();
    }
}