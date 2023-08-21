package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.member.SortCondition;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.profile.*;
import Dcoding.Celebrem.repository.LikesRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.TagRepository;
import Dcoding.Celebrem.service.LikesService;
import Dcoding.Celebrem.service.MemberService;
import Dcoding.Celebrem.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "프로필 API")
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final LikesRepository likesRepository;
    private final LikesService likesService;

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

    @Operation(summary = "메인 페이지 피드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 불러오기 성공")
    })
    @GetMapping("/feed")
    public ResponseEntity<List<FeedResponseDto>> getFeed(@RequestParam("page") int page, @RequestParam("tagName")String tagName, @RequestParam("orderBy")SortCondition sortCondition) {
        return ResponseEntity.ok(profileService.getFeed(tagName, page, sortCondition));
    }

    @Operation(summary = "인플루언서 좋아요(찜)", description = "좋아요가 눌린 상태라면 좋아요 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요(찜), 좋아요 취소 성공")
    })
    @PostMapping("profile/{profile_id}/likes")
    public ResponseEntity<Void> getLikesProfile(@PathVariable("profile_id")Long profileId) {
        likesService.like(profileId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "찜 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜 목록 불러오기 성공")
    })
    @GetMapping("/likes")
    public ResponseEntity<List<LikesResponseDto>> getLikesProfile() {
        return ResponseEntity.ok(likesService.getAllLikesProfile());
    }

}
