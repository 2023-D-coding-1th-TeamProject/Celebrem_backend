package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.BadRequestException;
import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.dto.member.MemberCreateRequestDto;
import Dcoding.Celebrem.dto.token.LoginDto;
import Dcoding.Celebrem.dto.token.token.TokenDto;
import Dcoding.Celebrem.dto.token.token.TokenRequestDto;
import Dcoding.Celebrem.common.jwt.RefreshToken;
import Dcoding.Celebrem.common.jwt.TokenProvider;
import Dcoding.Celebrem.dto.verify.NicknameVerifyRequestDto;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 이메일 중복 검사 + 닉네임 중복검사 메소드로 나눌 필요
     */
    @Transactional
    public void memberSignup(MemberCreateRequestDto memberCreateRequestDto) {
        if (memberRepository.existsMemberByEmail(memberCreateRequestDto.getEmail())) {
            throw new BadRequestException("이미 가입되어 있는 유저입니다");
        }
        Member member = memberCreateRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    public void verifyNicknameDuplication(NicknameVerifyRequestDto nicknameVerifyRequestDto) {
        if (memberRepository.existsMemberByNickname(nicknameVerifyRequestDto.getNickname())) {
            throw new BadRequestException("이미 사용하고 있는 닉네임입니다.");
        }
    }

    @Transactional
    public TokenDto memberLogin(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 인증 객체인 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        if (authenticationToken.equals(null)) throw new BadRequestException("로그인 정보가 올바르지 않습니다.");

        return getToken(authenticationToken);
    }

    /**
     * JWT 토큰 생성
     */
    public TokenDto getToken(UsernamePasswordAuthenticationToken authenticationToken){
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public void logout(){
        refreshTokenRepository.deleteRefreshTokenByKey(SecurityUtil.getCurrentMemberEmail());
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new UnauthorizedException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new BadRequestException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new UnauthorizedException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

}