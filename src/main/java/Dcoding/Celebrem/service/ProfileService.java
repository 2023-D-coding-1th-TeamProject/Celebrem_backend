package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.member.SortCondition;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.profile.FeedRequestDto;
import Dcoding.Celebrem.dto.profile.FeedResponseDto;
import Dcoding.Celebrem.dto.profile.UpdateProfileRequestDto;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import Dcoding.Celebrem.repository.TagRepository;
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

    /**
     * 인플루언서 등록하기
     * user_role -> 받은 정보로 등록
     * influencer_role -> 이미 등록된 사용자 -> 등록 불가
     */
    @Transactional
    public void registerInfluencer(Long memberId, String instagramId, Tag... tags) {
        // 엔티티 조회 : member는 빈 profile 객체를 가지고 있는 상태
        Member member = memberRepository.findById(memberId).get();
        Profile profile = profileRepository.findByMember_Id(memberId);

        // 인플루언서 등록 가능한 아이디인지 검사
        member.checkAuthorityToInfluencer();

        // 정보가 입력 된 프로필 객체 생성
        Profile getProfile = profile.registerInfluencer(instagramId, tags);

        // 등록
        member.registerInfluencer(getProfile);
    }

    /**
     *  프로필 업데이트 메소드
     */
    @Transactional
    public void updateProfile(Long profileId, UpdateProfileRequestDto updateProfileDto){
        Profile profile = findById(profileId);
        List<Tag> updateTags = new ArrayList<>();

        for(String tagName : updateProfileDto.getTagNames()){
            Tag tag = tagRepository.findByName(tagName).orElseThrow(
                    () -> new NotFoundException("해당 태그를 찾을 수 없습니다."));
            updateTags.add(tag);
        }

        profile.update(updateProfileDto.getProfileImageUrl(), updateProfileDto.getDescription(), updateProfileDto.getInstagramId(), updateTags);
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
