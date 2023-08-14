package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.verification.EmailVerification;
import Dcoding.Celebrem.dto.email.SendVerificationCodeRequestDto;
import Dcoding.Celebrem.dto.email.VerifyRequestDto;
import Dcoding.Celebrem.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public void verifyDuplication(VerifyRequestDto verifyRequestDto) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(verifyRequestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("잘못된 입력입니다."));
        emailVerification.verify(verifyRequestDto);
        // 인증 완료? -> DB에서 해당 row 삭제
    }

    @Transactional
    public void sendVerificationCode(SendVerificationCodeRequestDto recipient) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        EmailVerification emailVerification = findOrCreateEmailVerification(recipient.getEmail());

        String code = emailVerification.generateCode();

        helper.setFrom(sender, "Celebrem");
        helper.setTo(recipient.getEmail());
        helper.setSubject("Celebrem 회원 가입 인증 코드가 발송되었습니다.");

        String templateContent = readTemplateContent("email-template.html");
        templateContent = templateContent.replace("{{ePw}}", code);
        helper.setText(templateContent, true);
        javaMailSender.send(message);

        emailVerificationRepository.save(emailVerification);
    }

    private EmailVerification findOrCreateEmailVerification(String email) {
        Optional<EmailVerification> emailVerification = emailVerificationRepository.findByEmail(email);
        if (emailVerification.isEmpty()) {
            emailVerification = Optional.of(new EmailVerification(email));
        }

        return emailVerification.get();
    }

    private String readTemplateContent(String templateFileName) throws IOException {
        Resource resource = new ClassPathResource(templateFileName);
        Path filePath = resource.getFile().toPath();

        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    public void verify(VerifyRequestDto verifyRequestDto) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(verifyRequestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("잘못된 입력입니다."));
        emailVerification.verify(verifyRequestDto);
        emailVerificationRepository.delete(emailVerification); // 인증 완료시 DB 인증코드 데이터 삭제
    }

}
