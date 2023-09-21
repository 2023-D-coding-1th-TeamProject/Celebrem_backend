package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.member.SortCondition;
import Dcoding.Celebrem.dto.profile.*;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import Dcoding.Celebrem.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RegisterInfluencerResponseDto registerInfluencer(RegisterInfluencerRequestDto requestDto) {
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();

        profile.registerInfluencer(requestDto);
        return new RegisterInfluencerResponseDto(Authority.ROLE_INFLUENCER);
    }

    @Transactional
    public void updateProfile(UpdateProfileRequestDto updateProfileDto){
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();

        profile.update(updateProfileDto);
    }

    public InfluencerProfileResponseDto getInfluencerProfile(Long profileId) {
        Optional<Member> currentMember = memberRepository.findByEmailFetchLikes(SecurityUtil.getCurrentMemberEmail());
        Profile profile = profileRepository.findByIdFetch(profileId).orElseThrow(
                () -> new NotFoundException("프로필(아이디: " + profileId + ")를 찾을 수 없습니다."));
        if (currentMember.isEmpty()) {
            return profile.getInfluencerProfile(false);
        }
        return profile.getInfluencerProfile(currentMember.get().profileIsInLikes(profile));
    }

    public List<InfluencerProfileResponseDto> getLikeInfluencerProfile() {
        Member currentMember = memberRepository.findByEmailFetchLikes(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다"));
        List<Likes> likes = currentMember.getLikes();

        return likes.stream()
                .map(l -> l.getProfile().getInfluencerProfile(true))
                .collect(Collectors.toList());
    }

    public void updateProfileImage(UpdateProfileImageRequestDto updateProfileImageRequestDto) {
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();

        profile.changeProfileImage(updateProfileImageRequestDto);
    }

    public InfluencerProfileResponseDto getMyProfile() {
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();
        return profile.getInfluencerProfile(false);
    }

    public List<FeedResponseDto> getFeed(String tagName, int page, SortCondition sortCondition) {
        Optional<Member> member = memberRepository.findMemberByEmail(SecurityUtil.getCurrentMemberEmail());
        Pageable pageable = PageRequest.of(page - 1, 10);

        if (member.isEmpty()) return getFeedForNonMembers(tagName, pageable, sortCondition);
        return getFeedForMembers(member.get(), tagName, pageable, sortCondition);
    }

    private List<FeedResponseDto> getFeedForNonMembers(String tagName, Pageable pageable, SortCondition sortCondition) {
        if (sortCondition.equals(SortCondition.RANDOM)) {
            Page<Profile> profiles = profileRepository.findByTagNameFetch(tagName, pageable);
            List<FeedResponseDto> result = profiles.stream().map(p -> new FeedResponseDto(p)).collect(Collectors.toList());
            Collections.shuffle(result);
            return result;
        }
        Page<Profile> profiles = profileRepository.findByTagNameFetchOrderByLikeCount(tagName, pageable);
        return profiles.stream().map(p -> new FeedResponseDto(p)).collect(Collectors.toList());
    }

    private List<FeedResponseDto> getFeedForMembers(Member member, String tagName, Pageable pageable, SortCondition sortCondition) {
        if (sortCondition.equals(SortCondition.RANDOM)) {
            Page<Profile> profiles = profileRepository.findByTagNameFetch(tagName, pageable);
            List<FeedResponseDto> result = profiles.stream().map(p -> new FeedResponseDto(member, p)).collect(Collectors.toList());
            Collections.shuffle(result);
            return result;
        }
        Page<Profile> profiles = profileRepository.findByTagNameFetchOrderByLikeCount(tagName, pageable);
        return profiles.stream().map(p -> new FeedResponseDto(member, p)).collect(Collectors.toList());
    }

    public List<FeedResponseDto> getProfilesByNickname(String nickname) {
        Optional<Member> member = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail());
        if(member.isPresent()) return getProfilesByNicknameForMember(member.get(), nickname);
        return getProfilesByNicknameForNonMember(nickname);
    }

    private List<FeedResponseDto> getProfilesByNicknameForNonMember(String nickname) {
        List<Profile> profiles = profileRepository.findAllByNickname(nickname);
        return profiles.stream().map(p -> new FeedResponseDto(p)).collect(Collectors.toList());
    }

    private List<FeedResponseDto> getProfilesByNicknameForMember(Member member, String nickname) {
        List<Profile> profiles = profileRepository.findAllByNickname(nickname);
        return profiles.stream().map(p -> new FeedResponseDto(member, p)).collect(Collectors.toList());

    }
}
