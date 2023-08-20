package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.BadRequestException;
import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.tag.ProfileTag;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.tag.GetTagsResponseDto;
import Dcoding.Celebrem.dto.tag.TagSetupRequestDto;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileTagRepository;
import Dcoding.Celebrem.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static Dcoding.Celebrem.common.util.SecurityUtil.getCurrentMemberEmail;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ProfileTagRepository profileTagRepository;
    private final MemberRepository memberRepository;
    private final int MAX_TAG_COUNT = 3;

    @Transactional
    public void setUpProfileTags(TagSetupRequestDto tagSetupRequestDto) {
        Member currentMember = memberRepository.findByEmailFetchProfile(getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다"));
        List<String> tagNames = tagSetupRequestDto.getTagNames();

        if (tagNames.size() > MAX_TAG_COUNT) throw new BadRequestException("태그는 3개까지만 설정이 가능합니다.");

        currentMember.getProfile().clearTags();
        tagNames.stream().forEach(
                tagName -> {
                    Tag tag = tagRepository.findByName(tagName).orElseThrow(() -> new NotFoundException("없는 태그명입니다."));
                    profileTagRepository.save(new ProfileTag(currentMember.getProfile(), tag));
                }
        );
    }

    public GetTagsResponseDto getAllTags() {
        return new GetTagsResponseDto(tagRepository.findAll());
    }

}
