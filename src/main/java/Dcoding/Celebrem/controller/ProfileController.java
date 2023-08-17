package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.profile.UpdateProfileRequestDto;
import Dcoding.Celebrem.dto.profile.UpdateProfileResponseDto;
import Dcoding.Celebrem.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
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

}
