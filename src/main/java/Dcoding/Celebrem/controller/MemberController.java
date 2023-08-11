package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.domain.Member;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import Dcoding.Celebrem.dto.member.MemberPasswordUpdateRequestDto;
import Dcoding.Celebrem.service.AuthService;
import Dcoding.Celebrem.service.MemberService;
import Dcoding.Celebrem.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    @GetMapping("/me")
    public ResponseEntity<MemberCreateResponseDto> findCurrentMember(){
        return ResponseEntity.ok(Member.of(memberService.findMemberById(SecurityUtil.getCurrentMemberId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberCreateResponseDto> findMemberById(@PathVariable Long id){
        return ResponseEntity.ok(Member.of(memberService.findMemberById(id)));
    }

    @PutMapping("/me/updatePassword")
    public ResponseEntity<Boolean> updateMemberPassword(MemberPasswordUpdateRequestDto memberPasswordUpdateRequestDto){
        return ResponseEntity.ok(authService.updatePassword(memberService.findMemberById(SecurityUtil.getCurrentMemberId()), memberPasswordUpdateRequestDto));
    }
}
