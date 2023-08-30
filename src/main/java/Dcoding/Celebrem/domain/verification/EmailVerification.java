package Dcoding.Celebrem.domain.verification;

import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.dto.verify.EmailVerifyRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

@Entity
@NoArgsConstructor
@Getter
public class EmailVerification {

    @Id @GeneratedValue
    @Column(name = "email_verification_id")
    private Long id;
    private String email;
    private String code;
    private LocalDateTime expiredDate;

    public EmailVerification(String email) {
        this.email = email;
        this.expiredDate = LocalDateTime.now().plusMinutes(3);
    }

    public EmailVerification(String email, LocalDateTime expiredDate) {
        this.email = email;
        this.expiredDate = expiredDate;
    }

    public String generateCode() {
        final int CODE_LENGTH = 8;

        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) code.append((char) ((int) (random.nextInt(26)) + 65)); //  A~Z

        this.code = code.toString();
        return this.code;
    }

    public VerificationEventType verify(EmailVerifyRequestDto emailVerifyRequestDto) {
        if (!expiredDate.isAfter(LocalDateTime.now()) || !code.equals(emailVerifyRequestDto.getCode())) {
            throw new UnauthorizedException("인증번호가 올바르지 않습니다.");
        }
        if (!code.equals(emailVerifyRequestDto.getCode())) {
            return VerificationEventType.FAILURE;
        }
        return VerificationEventType.SUCCESS;
    }

}
