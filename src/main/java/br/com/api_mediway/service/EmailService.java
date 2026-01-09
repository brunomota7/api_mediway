package br.com.api_mediway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * envia o e-mail contendo o código de redefinição de senha.
     * este código expira em 3 minutos.
     */
    public void sendResetCodeEmail(String to, String name, String code) {
        log.info("[EMAIL] Sending password reset email to={} name={}", to, name);

        String subject = "Redefinição de Senha - Mediway";
        String body = String.format("""
                   Olá %s,
                
                   Seu código de redefinição de senha é: %s
                
                   Este código expira em 3 minutos. Se você não solicitou essa operação, ignore este e-mail.
                
                   Atenciosamente,
                   Equipe Mediway
                """, name, code);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("bruno7motadev@gmail.com");

            mailSender.send(message);
            log.info("[EMAIL] Password reset email successfully sent to={}", to);
        } catch (Exception ex) {
            log.error("[EMAIL] Failed to send email to={} - Error: {}", to, ex.getMessage(), ex);
            throw new RuntimeException("Failed to send email");
        }
    }
}
