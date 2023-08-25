package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.verification.EmailVerification;
import Dcoding.Celebrem.domain.verification.EmailVerificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationHistoryRepository extends JpaRepository<EmailVerificationHistory, Long> {
    EmailVerificationHistory findByEmailVerification(EmailVerification emailVerification);

}

