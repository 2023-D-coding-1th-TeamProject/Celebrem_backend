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

    @Operation(summary = "회원 가입", description = "이메일 인증, 닉네임 중복 검사 후 가입할 수 있음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입이 정상적으로 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 회원입니다."),
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignup(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        authService.memberSignup(memberCreateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "닉네임 중복 검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임입니다."),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임입니다."),
    })
    @PostMapping("/signup/nickname-verification")
    public ResponseEntity<Void> verifyNickname(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        authService.verifyNicknameDuplication(memberCreateRequestDto.getNickname());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "로그인", description = "AccessToken과 RefreshToken이 발급되며 스웨거 상단 Authorize 버튼을 눌러 AccessToken 값만 입력하면 로그인 상태가 된다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 정보가 올바르지 않습니다")
    })
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

    @Operation(summary = "인증 메일 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 메일 발송 완료"),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 이메일입니다"),
            @ApiResponse(responseCode = "500", description = "메일 발송에 문제가 발생했습니다"),
    })
    @PostMapping("/email-verification/send")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody SendVerificationCodeRequestDto sendVerificationCodeRequestDto) throws MessagingException, RuntimeException, IOException {
        emailVerificationService.verifyEmailDuplication(sendVerificationCodeRequestDto);
        emailVerificationService.sendVerificationCode(sendVerificationCodeRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이메일 인증", description = "발송된 인증 메일의 인증 코드를 통해 인증할 수 있고, 이메일 인증이 완료 되어야 회원 가입을 할 수 있다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 완료"),
            @ApiResponse(responseCode = "400", description = "이메일과 인증 코드가 올바른지 확인해주세요")
    })
    @PostMapping("/email-verification/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyRequestDto verifyRequestDto) {
        emailVerificationService.verify(verifyRequestDto);
        return ResponseEntity.noContent().build();
    }

}