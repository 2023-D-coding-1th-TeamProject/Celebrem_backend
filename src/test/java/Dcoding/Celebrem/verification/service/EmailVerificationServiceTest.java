package Dcoding.Celebrem.verification.service;

import Dcoding.Celebrem.common.exception.BadRequestException;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.verification.EmailVerification;
import Dcoding.Celebrem.domain.verification.EmailVerificationHistory;
import Dcoding.Celebrem.domain.verification.VerificationEventType;
import Dcoding.Celebrem.dto.verify.EmailVerifyRequestDto;
import Dcoding.Celebrem.dto.verify.SendVerificationCodeRequestDto;
import Dcoding.Celebrem.repository.EmailVerificationHistoryRepository;
import Dcoding.Celebrem.repository.EmailVerificationRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.service.email.EmailUtil;
import Dcoding.Celebrem.service.email.EmailVerificationService;
import jakarta.mail.MessagingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

@DataJpaTest
class EmailVerificationServiceTest {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailVerificationHistoryRepository emailVerificationHistoryRepository;

    @MockBean
    private EmailUtil emailUtil;

    private EmailVerificationService emailVerificationService;

    @BeforeEach
    private void setup() {
        this.emailVerificationService = new EmailVerificationService(emailUtil, emailVerificationRepository, memberRepository, emailVerificationHistoryRepository);
    }

    @DisplayName("이메일 인증번호 전송")
    @Test
    public void sendVerificationCodeAndVerifyTest() throws MessagingException, IOException {
        //given
        final String RECIPIENT_EMAIL = "nbw970508@gmail.com";
        final SendVerificationCodeRequestDto sendVerificationCodeRequestDto = new SendVerificationCodeRequestDto(RECIPIENT_EMAIL);
        emailVerificationService.sendVerificationCode(sendVerificationCodeRequestDto);

        // when
        boolean actual = emailVerificationRepository.existsByEmail(RECIPIENT_EMAIL);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("이메일 중복 검증")
    @Test
    public void verifyEmailDuplication() {
        // given
        final String EMAIL = "test001@gmail.com";
        Member member = Member.builder().email(EMAIL).nickname("test").password("test").build();
        memberRepository.save(member);

        final SendVerificationCodeRequestDto sendVerificationCodeRequestDto = new SendVerificationCodeRequestDto(EMAIL);

        // when, then
        Assertions.assertThatThrownBy(() ->
                        emailVerificationService.verifyEmailDuplication(sendVerificationCodeRequestDto))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("이메일 인증 성공")
    @Test
    public void emailVerificationFailureTest() throws MessagingException, IOException {
        // given
        final String EMAIL = "test001@gmail.com";
        SendVerificationCodeRequestDto sendVerificationCodeRequestDto = new SendVerificationCodeRequestDto(EMAIL);

        // when
        emailVerificationService.sendVerificationCode(sendVerificationCodeRequestDto);
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(sendVerificationCodeRequestDto.getEmail()).orElseThrow(
                () -> new BadRequestException("해당 이메일의 인증 정보가 없습니다."));
        EmailVerifyRequestDto verifyRequestDto = new EmailVerifyRequestDto(EMAIL, emailVerification.getCode());
        emailVerificationService.verify(verifyRequestDto);

        //then
        EmailVerificationHistory emailVerificationHistory = emailVerificationHistoryRepository.findByEmailVerification(emailVerification);
        Assertions.assertThat(emailVerificationHistory.getVerificationEventType()).isEqualTo(VerificationEventType.SUCCESS);
    }

}
