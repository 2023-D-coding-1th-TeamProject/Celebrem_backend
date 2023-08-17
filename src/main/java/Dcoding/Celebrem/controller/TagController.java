package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.tag.GetTagsResponseDto;
import Dcoding.Celebrem.dto.tag.TagSetupRequestDto;
import Dcoding.Celebrem.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "태그 API")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 설정", description = "태그는 최대 3개까지 설정 가능하고, 태그 설정시 기존 태그와 교체되는 방식(기존 태그는 사라짐)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "태그 설정 완료"),
            @ApiResponse(responseCode = "400", description = "태그는 3개까지만 설정이 가능합니다"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다"),
            @ApiResponse(responseCode = "404", description = "없는 태그명입니다")
    })
    @PostMapping("/profile-tag/setup")
    public ResponseEntity<Void> memberSignup(@RequestBody TagSetupRequestDto profileTagSetupReqeustDto) {
        tagService.setUpTags(profileTagSetupReqeustDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "태그 불러오기", description = "태그 설정을 위한 태그 정보 불러오기")
    @ApiResponse(responseCode = "200", description = "태그 불러오기 완료")
    @GetMapping("/tags")
    public ResponseEntity<GetTagsResponseDto> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

}
