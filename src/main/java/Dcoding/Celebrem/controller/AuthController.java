package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.email.SendVerificationCodeRequestDto;
import Dcoding.Celebrem.dto.email.VerifyRequestDto;
import Dcoding.Celebrem.dto.member.MemberCreateRequestDto;
import Dcoding.Celebrem.dto.token.LoginDto;
import Dcoding.Celebrem.dto.token.token.TokenDto;
import Dcoding.Celebrem.dto.token.token.TokenRequestDto;
import Dcoding.Celebrem.service.AuthService;
import Dcoding.Celebrem.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API")
public class AuthController {
    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;

    @Operation(summary = "회원 가입 이메일 인증 코드 발송", description = "이메일 중복 시에 DUPLICATED_EMAIL(3011)에러를 리턴하고 중복된 이메일이 아니라면 인증 코드 발송")
    @ApiResponse(responseCode = "3011", description = "중복된 이메일 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignup(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        authService.memberSignup(memberCreateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup/nickname-verification")
    public ResponseEntity<Void> verifyNickname(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        authService.verifyNicknameDuplication(memberCreateRequestDto.getNickname());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> memberLogin(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.memberLogin(loginDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PatchMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal User user, @RequestBody TokenDto tokenDto) {
        System.out.println(user.getAuthorities());
        return ResponseEntity.ok(authService.logout(tokenDto.getAccessToken(), user));
    }

    @PostMapping("/email-verification/send")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody SendVerificationCodeRequestDto sendVerificationCodeRequestDto) throws MessagingException, RuntimeException, IOException {
        emailVerificationService.verifyEmailDuplication(sendVerificationCodeRequestDto);
        emailVerificationService.sendVerificationCode(sendVerificationCodeRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyRequestDto verifyRequestDto) {
        emailVerificationService.verify(verifyRequestDto);
        return ResponseEntity.noContent().build();
    }

}