package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.tag.TagSetupRequestDto;
import Dcoding.Celebrem.service.TagService;
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

    @PostMapping("/profile-tag/setup")
    public ResponseEntity<Void> memberSignup(@RequestHeader("Authorization")String accessToken, @RequestBody TagSetupRequestDto profileTagSetupReqeustDto) {
        tagService.setUpTags(profileTagSetupReqeustDto);
        return ResponseEntity.noContent().build();
    }

}
