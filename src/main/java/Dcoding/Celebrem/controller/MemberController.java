package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.profile.InfluencerProfileResponseDto;
import Dcoding.Celebrem.dto.profile.RegisterInfluencerRequestDto;
import Dcoding.Celebrem.dto.profile.UpdateProfileRequestDto;
import Dcoding.Celebrem.dto.profile.*;
import Dcoding.Celebrem.dto.tag.TagSetupRequestDto;
import Dcoding.Celebrem.service.ProfileService;
import Dcoding.Celebrem.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원 API")
public class MemberController {

    private final TagService tagService;
    private final ProfileService profileService;

    @Operation(summary = "내 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다")
    })
    @GetMapping("/my-profile")
    public ResponseEntity<InfluencerProfileResponseDto> findMyProfileById() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @Operation(summary = "인플루언서 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인플루언서 등록 성공"),
            @ApiResponse(responseCode = "400", description = "태그는 3개까지만 설정이 가능합니다"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "없는 태그명입니다")
    })
    @PostMapping("/register-influencer")
    public ResponseEntity<RegisterInfluencerResponseDto> RegisterInfluencer(@RequestBody @Valid RegisterInfluencerRequestDto requestDto) {
        tagService.setUpProfileTags(new TagSetupRequestDto(requestDto.getTagNames()));
        return ResponseEntity.ok(profileService.registerInfluencer(requestDto));
    }

    @Operation(summary = "프로필 정보 수정을 위한 기존 프로필 정보 반환", description = "인플루언서가 아니라면 인플루언서 프로필 정보들은 null로 반환됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 정보 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
    })
    @GetMapping("/my-profile/update")
    public ResponseEntity<InfluencerProfileResponseDto> getProfileForUpdate() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @Operation(summary = "프로필 정보 수정", description = "tag 정보가 포함되어 있다면 인플루언서 프로필 수정, tag 정보가 없다면 광고주 프로필 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "태그는 3개까지만 설정이 가능합니다"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "없는 태그명입니다")
    })
    @PutMapping("/my-profile/update")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        if (updateProfileRequestDto.getTagNames() != null && !updateProfileRequestDto.getTagNames().isEmpty()) {
            tagService.setUpProfileTags(new TagSetupRequestDto(updateProfileRequestDto.getTagNames()));
        }
        profileService.updateProfile(updateProfileRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로필 이미지 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다" )
    })
    @PutMapping(path = "/my-profile/change-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfileImage(@RequestPart("image") MultipartFile image) throws IOException {
        profileService.updateProfileImage(image);
        return ResponseEntity.noContent().build();
    }

}
