package Dcoding.Celebrem.service;

import Dcoding.Celebrem.dto.email.SendVerificationCodeRequestDto;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailVerificationServiceTest {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Test
    public void sendVerificationCodeTest() throws MessagingException, IOException {
        final String RECIPIENT_EMAIL = "nbw970508@gmail.com";

        //given
        SendVerificationCodeRequestDto sendVerificationCodeRequestDto = new SendVerificationCodeRequestDto(RECIPIENT_EMAIL);

        // when, then
        emailVerificationService.sendVerificationCode(sendVerificationCodeRequestDto);

    }

}