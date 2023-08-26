package Dcoding.Celebrem.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailUtilImpl implements EmailUtil{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("emailVerificationTemplate.html")
    private String templateFileName;

    @Override
    public void sendVerificationCode(String email, String code) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(sender, "Celebrem");
        helper.setTo(email);
        helper.setSubject("Celebrem 회원 가입 인증 코드가 발송되었습니다.");

        String templateContent = readTemplateContent(templateFileName);
        templateContent = templateContent.replace("{{ePw}}", code);
        helper.setText(templateContent, true);
        javaMailSender.send(message);
    }

    private String readTemplateContent(String templateFileName) throws IOException {
        Resource resource = new ClassPathResource(templateFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
