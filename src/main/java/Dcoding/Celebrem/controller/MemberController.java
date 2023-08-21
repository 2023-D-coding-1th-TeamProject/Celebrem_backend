package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import Dcoding.Celebrem.dto.member.MemberProfileRequestDto;
import Dcoding.Celebrem.dto.member.MemberProfileResponseDto;
import Dcoding.Celebrem.service.AuthService;
import Dcoding.Celebrem.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<MemberCreateResponseDto> findCurrentMember(){
        return ResponseEntity.ok(Member.of(memberService.findByEmailFetchProfile(SecurityUtil.getCurrentMemberEmail())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberCreateResponseDto> findMemberById(@PathVariable Long id){
        return ResponseEntity.ok(Member.of(memberService.findMemberById(id)));
    }

    /**
     * 회원 개인정보 조회 -> 일반회원(자신) 프로필 화면
     */
    @GetMapping("/myProfile")
    public MemberProfileResponseDto findMyProfileById(@RequestBody @Valid MemberProfileRequestDto requestDto) {

        String email = SecurityUtil.getCurrentMemberEmail();
        Member member = memberService.findByEmailFetchProfile(email);
        Profile profile = member.getProfile();

        return new MemberProfileResponseDto(
                member.getNickname(),
                member.getEmail(),
                profile.getProfileImageUrl(),
                profile.getDescription(),
                profile.getInstagramId(),
                profile.getProfileTagNames());
    }

}
