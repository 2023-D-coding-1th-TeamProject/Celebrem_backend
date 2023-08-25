package Dcoding.Celebrem.service.email;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface EmailUtil {
    void sendVerificationCode(String email, String code) throws MessagingException, IOException;
}
