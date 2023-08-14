package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.email.SendVerificationCodeRequestDto;
import Dcoding.Celebrem.dto.email.VerifyRequestDto;
import Dcoding.Celebrem.dto.member.MemberCreateRequestDto;
import Dcoding.Celebrem.dto.token.LoginDto;
import Dcoding.Celebrem.dto.token.token.TokenDto;
import Dcoding.Celebrem.dto.token.token.TokenRequestDto;
import Dcoding.Celebrem.service.AuthService;
import Dcoding.Celebrem.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignup(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        authService.memberSignup(memberCreateRequestDto);
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
    public ResponseEntity<Void> sendVerificationCode(@RequestBody SendVerificationCodeRequestDto emailVerificationRequestDto)
            throws MessagingException, RuntimeException {
        try {
            emailVerificationService.sendVerificationCode(emailVerificationRequestDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyRequestDto verifyRequestDto) {
        emailVerificationService.verify(verifyRequestDto);
        return ResponseEntity.noContent().build();
    }

}