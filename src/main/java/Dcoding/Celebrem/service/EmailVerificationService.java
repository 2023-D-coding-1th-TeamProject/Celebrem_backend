package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.BadRequestException;
import Dcoding.Celebrem.domain.verification.EmailVerification;
import Dcoding.Celebrem.dto.email.SendVerificationCodeRequestDto;
import Dcoding.Celebrem.dto.email.VerifyRequestDto;
import Dcoding.Celebrem.repository.EmailVerificationRepository;
import Dcoding.Celebrem.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("emailVerificationTemplate.html")
    private String templateFileName;

    public void verifyEmailDuplication(SendVerificationCodeRequestDto sendVerificationCodeRequestDto) {
        if (memberRepository.existsMemberByEmail(sendVerificationCodeRequestDto.getEmail())) {
            throw new IllegalArgumentException("중복되는 이메일이 존재합니다.");
        }
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

        String templateContent = readTemplateContent(templateFileName);
        templateContent = templateContent.replace("{{ePw}}", code);
        helper.setText(templateContent, true);
        javaMailSender.send(message);

        emailVerificationRepository.save(emailVerification);
    }

    public void verify(VerifyRequestDto verifyRequestDto) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(verifyRequestDto.getEmail()).orElseThrow(
                () -> new BadRequestException("잘못된 입력입니다.")); // TODO : customException Refactor
        emailVerification.verify(verifyRequestDto);
        emailVerificationRepository.delete(emailVerification); // TODO : 인증 완료시 DB 인증코드 데이터 삭제 (왜 안됨?)
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

}
