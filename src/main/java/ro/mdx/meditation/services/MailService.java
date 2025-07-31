package ro.mdx.meditation.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendAuthenticationInformationEmail(String to, String username, String password) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password", password);

        String htmlContent = templateEngine.process("email/auth-information.html", context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setTo(to);
            helper.setSubject("Your Admin Account Credentials");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.error(ex.getMessage());
        }
    }
}
