package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.member.SortCondition;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.profile.RegisterInfluencerRequestDto;
import Dcoding.Celebrem.dto.profile.RegisterInfluencerResponseDto;
import Dcoding.Celebrem.dto.profile.FeedRequestDto;
import Dcoding.Celebrem.dto.profile.FeedResponseDto;
import Dcoding.Celebrem.dto.profile.UpdateProfileRequestDto;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import Dcoding.Celebrem.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    @Transactional
    public RegisterInfluencerResponseDto registerInfluencer(Long memberId, RegisterInfluencerRequestDto requestDto) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("회원(아이디:  " + memberId + ")를 찾을 수 없습니다."));
        Profile profile = profileRepository.findByMember_Id(memberId);

        member.checkAuthorityToInfluencer();

        List<Tag> tags = new ArrayList<>();
        for (String tagName : requestDto.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName).orElseThrow(
                    () -> new NotFoundException("없는 태그명입니다.")
            );
            tags.add(tag);
        }

        Profile getProfile = profile.registerInfluencer(requestDto.getInstagramId(), (Tag) tags);

        member.registerInfluencer(getProfile);

        return new RegisterInfluencerResponseDto(
                member.getNickname(),
                member.getEmail(),
                profile.getProfileImageUrl(),
                profile.getDescription(),
                profile.getInstagramId(),
                profile.getProfileTagNames());
    }

    @Transactional
    public void updateProfile(Long profileId, UpdateProfileRequestDto updateProfileDto){
        Profile profile = findProfileById(profileId);
        List<Tag> updateTags = new ArrayList<>();

        for(String tagName : updateProfileDto.getTagNames()){
            Tag tag = tagRepository.findByName(tagName).orElseThrow(
                    () -> new NotFoundException("해당 태그를 찾을 수 없습니다."));
            updateTags.add(tag);
        }

        profile.update(updateProfileDto.getProfileImageUrl(), updateProfileDto.getDescription(), updateProfileDto.getInstagramId(), updateTags);
    }

    public Profile findProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(
                () -> new NotFoundException("프로필(아이디: " + profileId + ")를 찾을 수 없습니다."));

        return profile;
    }

    public List<FeedResponseDto> getFeed(String tagName, int page, SortCondition sortCondition) {
        Optional<Member> member = memberRepository.findMemberByEmail(SecurityUtil.getCurrentMemberEmail());
        Pageable pageable = PageRequest.of(page - 1, 10);

        if (member.isEmpty()) return getFeedForNonMembers(tagName, pageable, sortCondition);

        return getFeedForMembers(member.get(), tagName, pageable, sortCondition);

    }

    public List<FeedResponseDto> getFeedForNonMembers(String tagName, Pageable pageable, SortCondition sortCondition) {
        if (sortCondition.equals(SortCondition.RANDOM)) {
            Page<Profile> profiles = profileRepository.findByTagNameFetch(tagName, pageable);
            List<FeedResponseDto> result = profiles.stream().map(p ->
                    FeedResponseDto.builder().nickname(p.getMember().getNickname())
                            .imageUrl(p.getProfileImageUrl())
                            .likeCount(p.getLikeCount())
                            .isLike(false)
                            .tagNames(p.getProfileTags().stream().map(pt -> pt.getTag().getName()).collect(Collectors.toList()))
                            .build()).collect(Collectors.toList());
            Collections.shuffle(result);
            return result;
        }
        Page<Profile> profiles = profileRepository.findByTagNameFetchOrderByLikeCount(tagName, pageable);
        return profiles.stream().map(p ->
                FeedResponseDto.builder().nickname(p.getMember().getNickname())
                        .imageUrl(p.getProfileImageUrl())
                        .likeCount(p.getLikeCount())
                        .isLike(false)
                        .build()).collect(Collectors.toList());

    }

    public List<FeedResponseDto> getFeedForMembers(Member member, String tagName, Pageable pageable, SortCondition sortCondition) {
        if (sortCondition.equals(SortCondition.RANDOM)) {
            Page<Profile> profiles = profileRepository.findByTagNameFetch(tagName, pageable);
            List<FeedResponseDto> result = profiles.stream().map(p ->
                    FeedResponseDto.builder().nickname(p.getMember().getNickname())
                            .imageUrl(p.getProfileImageUrl())
                            .likeCount(p.getLikeCount())
                            .isLike(member.getProfile().getProfileTags().contains(p))
                            .tagNames(p.getProfileTags().stream().map(pt -> pt.getTag().getName()).collect(Collectors.toList()))
                            .build()).collect(Collectors.toList());
            Collections.shuffle(result);
            return result;
        }
        Page<Profile> profiles = profileRepository.findByTagNameFetchOrderByLikeCount(tagName, pageable);
        return profiles.stream().map(p ->
                FeedResponseDto.builder().nickname(p.getMember().getNickname())
                        .imageUrl(p.getProfileImageUrl())
                        .likeCount(p.getLikeCount())
                        .isLike(member.getProfile().getProfileTags().contains(p))
                        .build()).collect(Collectors.toList());
    }
}
