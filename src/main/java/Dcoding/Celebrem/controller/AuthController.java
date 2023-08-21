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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "회원가입", description = "사용자 입력값 기반 회원가입. 사용자 신규 생성. 이메일 중복 시 ")
    @ApiResponse(responseCode = "400", description = "이미 가입되어 있는 유저입니다")
    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignup(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        authService.memberSignup(memberCreateRequestDto);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "회원 가입 닉네임 중복 검증", description = "닉네임 중복 시에 에러 리턴")
    @ApiResponse(responseCode = "400", description = "이미 사용하고 있는 닉네임입니다.")
    @PostMapping("/signup/nickname-verification")
    public ResponseEntity<Void> verifyNickname(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        authService.verifyNicknameDuplication(memberCreateRequestDto.getNickname());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "사용자 로그인", description = "사용자 로그인 성공시 토큰 발급.")
    @ApiResponse(responseCode = "401", description = "이메일 혹은 비밀번호가 틀렸습니다.")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> memberLogin(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.memberLogin(loginDto));
    }

    @Operation(summary = "리프레시 토큰으로 만료된 액세스 토큰 재발급", description = "재발급 성공시 토큰 발급")
    @ApiResponse(responseCode = "401", description = "잘못된 토큰입니다.")
    @ApiResponse(responseCode = "400", description = "이미 로그아웃된 사용자입니다.")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto)); // TODO : 리프레시 토큰도 재발급?? 논의 필요
    }

    @Operation(summary = "사용자 로그아웃", description = "로그아웃 성공 시 성공 메시지 전송")
    @ApiResponse(responseCode = "404", description = "로그인 되지 않은 사용자 정보입니다.")
    @PatchMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal User user, @RequestBody TokenDto tokenDto) {
        System.out.println(user.getAuthorities());
        return ResponseEntity.ok(authService.logout(tokenDto.getAccessToken(), user));
    }


    @Operation(summary = "이메일 인증번호 발송", description = "이메일 중복시 에러 리턴, 이메일 인증번호 발송")
    @ApiResponse(responseCode = "3011", description = "중복된 닉네임 입니다.")
    @PostMapping("/email-verification/send")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody SendVerificationCodeRequestDto sendVerificationCodeRequestDto) throws MessagingException, RuntimeException, IOException {
        emailVerificationService.verifyEmailDuplication(sendVerificationCodeRequestDto);
        emailVerificationService.sendVerificationCode(sendVerificationCodeRequestDto);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "이메일 인증코드 검증", description = "이메일 인증번호 미일치 시 에러 반환")
    @ApiResponse(responseCode = "400", description = "잘못된 이메일 입력입니다.")
    @ApiResponse(responseCode = "401", description = "인증번호가 올바르지 않습니다.")
    @PostMapping("/email-verification/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyRequestDto verifyRequestDto) {
        emailVerificationService.verify(verifyRequestDto);
        return ResponseEntity.noContent().build();
    }

}