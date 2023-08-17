package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.dto.email.SendVerificationCodeRequestDto;
import Dcoding.Celebrem.repository.EmailVerificationRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import jakarta.mail.MessagingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailVerificationServiceTest {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일 인증번호 전송, emailVerification 엔티티 저장 테스트")
    @Test
    public void sendVerificationCodeAndVerifyTest() throws MessagingException, IOException {
        final String RECIPIENT_EMAIL = "nbw970508@gmail.com";

        //given
        SendVerificationCodeRequestDto sendVerificationCodeRequestDto = new SendVerificationCodeRequestDto(RECIPIENT_EMAIL);
        emailVerificationService.sendVerificationCode(sendVerificationCodeRequestDto);

        // when
        boolean actual = emailVerificationRepository.existsByEmail(RECIPIENT_EMAIL);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("중복된 이메일 검증 테스트")
    @Test
    public void verifyEmailDuplication() {
        final String EMAIL = "test001@gmail.com";

        // given
        SendVerificationCodeRequestDto sendVerificationCodeRequestDto = new SendVerificationCodeRequestDto(EMAIL);
        Member member = Member.builder()
                .email(EMAIL)
                .nickname("test")
                .phoneNumber("test")
                .password("test")
                .build();
        memberRepository.save(member);

        // when, then
        Assertions.assertThatThrownBy(() ->
                emailVerificationService.verifyEmailDuplication(sendVerificationCodeRequestDto))
                .isInstanceOf(IllegalArgumentException.class);
    }


}