package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.member.SortCondition;
import Dcoding.Celebrem.dto.profile.FeedResponseDto;
import Dcoding.Celebrem.dto.profile.UpdateProfileRequestDto;
import Dcoding.Celebrem.dto.profile.UpdateProfileResponseDto;
import Dcoding.Celebrem.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로필 API")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 프로필 수정하기
     */
    @PutMapping("/update/{id}")
    public UpdateProfileResponseDto updateProfile(@PathVariable("id") Long profileId,
                                                  @RequestBody @Valid UpdateProfileRequestDto requestDto) {

        profileService.updateProfile(profileId, requestDto);
        Profile findProfile = profileService.findById(profileId);

        return findProfile.UpdateProfileResponseDto();
    }

    /**
     * 인플루언서 등록하기
     */


    /**
     * 인플루언서 프로필 조회
     */
    @Operation(summary = "메인 페이지 피드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 불러오기 완료")
    })
    @GetMapping("/feed")
    public ResponseEntity<List<FeedResponseDto>> getFeed(@RequestParam("page") int page, @RequestParam("tagName")String tagName, @RequestParam("orderBy")SortCondition sortCondition) {
        return ResponseEntity.ok(profileService.getFeed(tagName, page, sortCondition));
    }

}
