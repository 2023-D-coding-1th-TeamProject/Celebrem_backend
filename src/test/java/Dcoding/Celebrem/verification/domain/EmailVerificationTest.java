package Dcoding.Celebrem.verification.domain;

import Dcoding.Celebrem.domain.verification.EmailVerification;
import Dcoding.Celebrem.domain.verification.VerificationEventType;
import Dcoding.Celebrem.dto.email.VerifyRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class EmailVerificationTest {

    @Test
    @DisplayName("이메일 인증 성공")
    public void verifySuccessTest() {
        // given
        final String RECIPIENT_EMAIL = "nbw970508@gmail.com";
        final EmailVerification emailVerification = new EmailVerification(RECIPIENT_EMAIL);
        final String code = emailVerification.generateCode();
        final VerifyRequestDto verifyRequestDto = new VerifyRequestDto(RECIPIENT_EMAIL, code);

        // when
        VerificationEventType actual = emailVerification.verify(verifyRequestDto);

        // then
        Assertions.assertThat(actual).isEqualTo(VerificationEventType.SUCCESS);
    }

    @Test
    @DisplayName("이메일 인증 실패(code값 불일치)")
    public void verifyFailTest() {
        // given
        final String RECIPIENT_EMAIL = "nbw970508@gmail.com";
        final EmailVerification emailVerification = new EmailVerification(RECIPIENT_EMAIL);
        final String code = emailVerification.generateCode() + "fail";
        final VerifyRequestDto verifyRequestDto = new VerifyRequestDto(RECIPIENT_EMAIL, code);

        // when
        VerificationEventType actual = emailVerification.verify(verifyRequestDto);

        // then
        Assertions.assertThat(actual).isEqualTo(VerificationEventType.FAILURE);
    }

    @Test
    @DisplayName("이메일 인증 실패(인증 시간 만료)")
    public void verifyExpiredTest() {
        // given
        final String RECIPIENT_EMAIL = "nbw970508@gmail.com";
        final EmailVerification emailVerification = new EmailVerification(RECIPIENT_EMAIL, LocalDateTime.now());
        final String code = emailVerification.generateCode();
        final VerifyRequestDto verifyRequestDto = new VerifyRequestDto(RECIPIENT_EMAIL, code);

        // when
        VerificationEventType actual = emailVerification.verify(verifyRequestDto);

        // then
        Assertions.assertThat(actual).isEqualTo(VerificationEventType.EXPIRED);
    }
}