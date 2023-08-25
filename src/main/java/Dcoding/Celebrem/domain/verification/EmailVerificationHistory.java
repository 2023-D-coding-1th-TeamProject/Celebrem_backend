package Dcoding.Celebrem.domain.verification;

import Dcoding.Celebrem.domain.member.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class EmailVerificationHistory {
    @Id
    @GeneratedValue
    @Column(name = "email_verification_history_id")
    private Long id;
    private LocalDateTime attemptAt;

    @Enumerated(EnumType.STRING)
    private VerificationEventType verificationEventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_verification_id")
    private EmailVerification emailVerification;

    public EmailVerificationHistory(VerificationEventType verificationEventType, EmailVerification emailVerification) {
        this.attemptAt = LocalDateTime.now();
        this.emailVerification = emailVerification;
        this.verificationEventType = verificationEventType;
    }

    public boolean eventTypeTest(VerificationEventType verificationEventType) {
        return this.verificationEventType.equals(verificationEventType) ;
    }
}

