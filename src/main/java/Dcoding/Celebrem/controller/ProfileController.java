package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.domain.member.SortCondition;
import Dcoding.Celebrem.dto.profile.*;
import Dcoding.Celebrem.service.LikesService;
import Dcoding.Celebrem.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로필 API")
public class ProfileController {

    private final ProfileService profileService;
    private final LikesService likesService;

    @Operation(summary = "인플루언서 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인플루언서 프로필 조회 성공"),
            @ApiResponse(responseCode = "404", description = "잘못된 입력입니다")
    })
    @GetMapping("/profile/{profile_id}")
    public ResponseEntity<InfluencerProfileResponseDto> getInfluencerProfile(@PathVariable("profile_id") Long profileId) {
        return ResponseEntity.ok(profileService.getInfluencerProfile(profileId));
    }

    @Operation(summary = "닉네임 기반 프로필 검색")
    @ApiResponse(responseCode = "200", description = "프로필 불러오기 성공")
    @GetMapping("/feed/{nickname}")
    public ResponseEntity<List<FeedResponseDto>> getProfilesByNickname(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(profileService.getProfilesByNickname(nickname));
    }

    @Operation(summary = "메인 페이지 피드")
    @ApiResponse(responseCode = "200", description = "피드 불러오기 성공")
    @GetMapping("/feed")
    public ResponseEntity<List<FeedResponseDto>> getFeed(@RequestParam(value = "page", defaultValue = "1", required = false) int page, @RequestParam(value = "tagName", required = false)String tagName, @RequestParam(value = "orderBy", required = false)SortCondition sortCondition) {
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

}
