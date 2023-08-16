package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.tag.ProfileTagSetupReqeustDto;
import Dcoding.Celebrem.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/profile-tag/setup")
    public ResponseEntity<Void> memberSignup(@RequestBody ProfileTagSetupReqeustDto profileTagSetupReqeustDto) {
//        tagService.profileTagSetup();
        return ResponseEntity.noContent().build();
    }

}
