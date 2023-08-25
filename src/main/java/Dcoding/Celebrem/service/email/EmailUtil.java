package Dcoding.Celebrem.service.email;

public interface EmailUtil {
    void sendVerificationCode(String email, String code);
}
