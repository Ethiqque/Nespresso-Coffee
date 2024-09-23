package com.nespresso.email.message;

import com.nespresso.email.dto.EmailTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EmailConfirmMessage implements MessageBuilder<EmailTokenDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EmailTokenDto event, Locale locale) {
        return messageSource.getMessage("email-template", new Object[]{event.token()}, locale);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == EmailConfirmMessage.class;
    }
}
