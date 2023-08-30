package Dcoding.Celebrem.domain.verification;

import Dcoding.Celebrem.dto.verify.EmailVerifyRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailVerificationTest {

    @Test
    @DisplayName("이메일 인증 테스트")
    public void verifyTest() {

        // given
        String RECIPIENT_EMAIL = "nbw970508@gmail.com";

        EmailVerification emailVerification = new EmailVerification(RECIPIENT_EMAIL);
        String code = emailVerification.generateCode();
        EmailVerifyRequestDto emailVerifyRequestDto = new EmailVerifyRequestDto(RECIPIENT_EMAIL, code);

        //when, then
        emailVerification.verify(emailVerifyRequestDto);

    }
}