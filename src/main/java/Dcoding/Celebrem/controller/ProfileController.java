package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.domain.member.SortCondition;
import Dcoding.Celebrem.dto.profile.*;
import Dcoding.Celebrem.dto.tag.TagSetupRequestDto;
import Dcoding.Celebrem.service.LikesService;
import Dcoding.Celebrem.service.MemberService;
import Dcoding.Celebrem.service.ProfileService;
import Dcoding.Celebrem.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "프로필 API")
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;
    private final LikesService likesService;
    private final TagService tagService;

    @Operation(summary = "프로필 정보 수정을 위한 기존 프로필 정보 반환", description = "인플루언서가 아니라면 인플루언서 프로필 정보들은 null로 반환됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 정보 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
    })
    @GetMapping("/profile/update")
    public ResponseEntity<InfluencerProfileResponseDto> getProfileForUpdate(@RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @Operation(summary = "프로필 정보 수정", description = "tag 정보가 포함되어 있다면 인플루언서 프로필 수정, tag 정보가 없다면 광고주 프로필 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "태그는 3개까지만 설정이 가능합니다"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "없는 태그명입니다")
    })
    @PutMapping("/profile/update")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        if (updateProfileRequestDto.getTagNames() != null && !updateProfileRequestDto.getTagNames().isEmpty()) {
            tagService.setUpTags(new TagSetupRequestDto(updateProfileRequestDto.getTagNames()));
        }
        profileService.updateProfile(updateProfileRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "인플루언서 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인플루언서 등록 성공"),
            @ApiResponse(responseCode = "400", description = "태그는 3개까지만 설정이 가능합니다"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "없는 태그명입니다")
    })
    @PostMapping("/register-influencer")
    public ResponseEntity<Void> RegisterInfluencer(@RequestBody @Valid RegisterInfluencerRequestDto requestDto) {
        tagService.setUpTags(new TagSetupRequestDto(requestDto.getTagNames()));
        profileService.registerInfluencer(requestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "인플루언서 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인플루언서 프로필 조회 성공"),
            @ApiResponse(responseCode = "404", description = "잘못된 입력입니다")
    })
    @GetMapping("/profile/{profile_id}")
    public ResponseEntity<InfluencerProfileResponseDto> getInfluencerProfile(@PathVariable("profile_id") Long profileId) {
        return ResponseEntity.ok(profileService.getInfluencerProfile(profileId));
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
