package com.nespresso.email.sender;

import com.nespresso.email.dto.EmailTokenDto;
import com.nespresso.email.message.EmailConfirmMessage;
import com.nespresso.email.message.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthTokenEmailConfirmation extends AbstractEmailSender<EmailTokenDto> {

    @Value("${spring.mail.subject.confirmation}")
    private String subject;

    @Autowired
    public AuthTokenEmailConfirmation(JavaMailSender javaMailSender,
                                      SimpleMailMessage mailMessage,
                                      List<MessageBuilder<EmailTokenDto>> messageBuilders) {
        super(javaMailSender, mailMessage, messageBuilders);
    }

    public void sendTemporaryCode(String email, String message) {
        String buildMessage = getMessage(EmailConfirmMessage.class, new EmailTokenDto(message));
        sendNotification(email, buildMessage, subject);
    }
}
