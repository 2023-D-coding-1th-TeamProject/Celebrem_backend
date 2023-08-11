package Dcoding.Celebrem.controller;

import Dcoding.Celebrem.dto.member.MemberCreateRequestDto;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import Dcoding.Celebrem.dto.token.LoginDto;
import Dcoding.Celebrem.dto.token.token.TokenDto;
import Dcoding.Celebrem.dto.token.token.TokenRequestDto;
import Dcoding.Celebrem.jwt.TokenProvider;
import Dcoding.Celebrem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final TokenProvider jwtTokenProvider;

    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicated(@PathVariable String email){
        return ResponseEntity.ok(authService.checkEmailDuplicated(email));
    }

    @GetMapping("/user-names/{name}/exists")
    public ResponseEntity<Boolean> checkNameDuplicated(@PathVariable String name){
        return ResponseEntity.ok(authService.checkNicknameDuplicated(name));
    }


    @PostMapping("/signup")
    public ResponseEntity<MemberCreateResponseDto> memberSignup(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        return ResponseEntity.ok(authService.memberSignup(memberCreateRequestDto));
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
}