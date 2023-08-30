package Dcoding.Celebrem.service.email;

import Dcoding.Celebrem.common.exception.BadRequestException;
import Dcoding.Celebrem.domain.verification.EmailVerification;
import Dcoding.Celebrem.domain.verification.EmailVerificationHistory;
import Dcoding.Celebrem.domain.verification.VerificationEventType;
import Dcoding.Celebrem.dto.verify.EmailVerifyRequestDto;
import Dcoding.Celebrem.dto.verify.SendVerificationCodeRequestDto;
import Dcoding.Celebrem.repository.EmailVerificationHistoryRepository;
import Dcoding.Celebrem.repository.EmailVerificationRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailUtil emailUtil;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberRepository memberRepository;
    private final EmailVerificationHistoryRepository emailVerificationHistoryRepository;

    public void verifyEmailDuplication(SendVerificationCodeRequestDto sendVerificationCodeRequestDto) {
        if (memberRepository.existsMemberByEmail(sendVerificationCodeRequestDto.getEmail())) {
            throw new BadRequestException("중복되는 이메일이 존재합니다.");
        }
    }

    @Transactional
    public void sendVerificationCode(SendVerificationCodeRequestDto recipient) throws MessagingException, IOException {
        EmailVerification emailVerification = findOrCreateEmailVerification(recipient.getEmail());
        emailUtil.sendVerificationCode(recipient.getEmail(), emailVerification.generateCode());
        emailVerificationRepository.save(emailVerification);
    }

    @Transactional
    public void verify(EmailVerifyRequestDto verifyRequestDto) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(verifyRequestDto.getEmail()).orElseThrow(
                () -> new BadRequestException("잘못된 입력입니다.")); // TODO : customException Refactor
        VerificationEventType verificationEventType = emailVerification.verify(verifyRequestDto);

        emailVerificationHistoryRepository.save(new EmailVerificationHistory(verificationEventType, emailVerification));
        verificationEventType.execute();
    }

    private EmailVerification findOrCreateEmailVerification(String email) {
        Optional<EmailVerification> emailVerification = emailVerificationRepository.findByEmail(email);
        if (emailVerification.isEmpty()) {
            emailVerification = Optional.of(new EmailVerification(email));
        }

        return emailVerification.get();
    }

}

