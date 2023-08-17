package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import Dcoding.Celebrem.service.AuthService;
import Dcoding.Celebrem.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

}
