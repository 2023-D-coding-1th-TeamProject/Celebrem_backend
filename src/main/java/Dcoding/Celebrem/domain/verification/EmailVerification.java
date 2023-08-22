package Dcoding.Celebrem.domain.verification;

import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.dto.email.VerifyRequestDto;
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
    }

    public String generateCode() {
        final int CODE_LENGTH = 8;

        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) code.append((char) ((int) (random.nextInt(26)) + 65)); //  A~Z

        this.code = code.toString();
        this.expiredDate = LocalDateTime.now().plusMinutes(3);
        return code.toString();
    }

    public VerificationEventType verify(VerifyRequestDto verifyRequestDto) {
        if (!expiredDate.isAfter(LocalDateTime.now())) {
            return VerificationEventType.FAILURE;
        }
        if (!code.equals(verifyRequestDto.getCode())) {
            return VerificationEventType.EXPIRED;
        }
        return VerificationEventType.SUCCESS;
    }

    public String getCodeForTest() {
        return this.code;
    }

}
