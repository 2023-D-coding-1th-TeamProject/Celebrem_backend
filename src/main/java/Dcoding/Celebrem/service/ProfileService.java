package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.common.util.SecurityUtil;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3UploadUtil s3UploadUtil;

    @Transactional
    public void registerInfluencer(RegisterInfluencerRequestDto requestDto) {
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();

        profile.registerInfluencer(requestDto);
    }

    @Transactional
    public void updateProfile(UpdateProfileRequestDto updateProfileDto){
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();

        profile.update(updateProfileDto);
    }

    public InfluencerProfileResponseDto getInfluencerProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(
                () -> new NotFoundException("프로필(아이디: " + profileId + ")를 찾을 수 없습니다."));
        return profile.getInfluencerProfile();
    }

    @Transactional
    public void updateProfileImage(MultipartFile image) throws IOException {
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();
        if (profile.getProfileImageUrl() != null && !profile.getProfileImageUrl().isEmpty()) s3UploadUtil.fileDelete(profile.getProfileImageUrl());

        String imageUrl = s3UploadUtil.upload(image);
        profile.changeProfileImage(imageUrl);
    }

    public InfluencerProfileResponseDto getMyProfile() {
        Profile profile = memberRepository.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다")).getProfile();
        return profile.getInfluencerProfile();
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
                            .tagNames(p.getProfileTagNames())
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
                        .tagNames(p.getProfileTagNames())
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
                            .tagNames(p.getProfileTagNames())
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
                        .tagNames(p.getProfileTagNames())
                        .build()).collect(Collectors.toList());
    }
}
