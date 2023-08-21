package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.controller.MemberController.Result;
import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.profile.*;
import Dcoding.Celebrem.repository.LikesRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.TagRepository;
import Dcoding.Celebrem.service.MemberService;
import Dcoding.Celebrem.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final LikesRepository likesRepository;

    /**
     * 프로필 수정하기
     */
    @PutMapping("/update")
    public UpdateProfileResponseDto updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody @Valid UpdateProfileRequestDto requestDto) {

        String email = SecurityUtil.getCurrentMemberEmail();
        Member member = memberService.findByEmailFetchProfile(email);
        Long profileId = member.getProfile().getId();

        profileService.updateProfile(profileId, requestDto);
        Profile findProfile = profileService.findProfileById(profileId);

        return findProfile.UpdateProfileResponseDto();
    }

    /**
     * 인플루언서 등록하기
     */
    @PutMapping("/register/{id}")
    public RegisterInfluencerResponseDto RegisterInfluencer(@RequestBody @Valid RegisterInfluencerRequestDto requestDto) {

        String email = SecurityUtil.getCurrentMemberEmail();
        Member member = memberService.findByEmailFetchProfile(email);

        List<Tag> tags = new ArrayList<>();
        for (String tagName : requestDto.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName).orElseThrow(
                    () -> new NotFoundException("없는 태그명입니다.")
            );
            tags.add(tag);
        }

        return profileService.registerInfluencer(member.getId(), requestDto);
    }


    /**
     * 인플루언서 프로필 조회
     */
    @GetMapping("/influencerProfile")
    public InfluencerProfileResponseDto influencerProfile(@RequestBody @Valid InfluencerProfileRequestDto requestDto) {

        Member member = memberService.findByIdFetchProfile(requestDto.getMemberId());
        Profile profile = member.getProfile();
        return new InfluencerProfileResponseDto(
                member.getNickname(),
                member.getEmail(),
                profile.getProfileImageUrl(),
                profile.getDescription(),
                profile.getProfileTagNames());
    }

    /**
     * 유저의 찜 목록 반환하기
     *
     */
    @GetMapping("/likes")
    public Result likeList() {
        // 현재 회원정보 가져오기
        String email = SecurityUtil.getCurrentMemberEmail();
        Member member = memberService.findByEmailFetchProfile(email);

        // 현재 회원이 좋아요 표시한
        List<Likes> likesList = likesRepository.findAllByIdFetchProfile(member.getId());
        List<LikesListResponseDto> collect =likesList.stream()
                .map(l -> new LikesListResponseDto(l.getMember(), l.getProfile()))
                .collect(Collectors.toList());

        return new Result<>(collect);
    }
}
