package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.tag.TagSetupRequestDto;
import Dcoding.Celebrem.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class TagController {

    private final TagService tagService;

    @Operation(summary = "인플루언서 프로필 태그 등록", description = "복수개의 태그 이름을 받아 프로필 태그 등록")
    @ApiResponse(responseCode = "400", description = "태그는 3개까지만 설정이 가능합니다.")
    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.")
    @ApiResponse(responseCode = "404", description = "없는 태그명입니다.")
    @PostMapping("/profile-tag/setup")
    public ResponseEntity<Void> setUpProfileTags(@RequestHeader("Authorization")String accessToken,
                                                 @RequestBody TagSetupRequestDto profileTagSetupReqeustDto) {
        tagService.setUpProfileTags(profileTagSetupReqeustDto);
        return ResponseEntity.noContent().build();
    }

}
