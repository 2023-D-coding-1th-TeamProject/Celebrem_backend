package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.verification.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmail(String email);
    boolean existsByEmail(String email);

}
