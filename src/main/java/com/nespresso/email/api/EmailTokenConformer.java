package com.nespresso.email.api;

import com.nespresso.email.api.token.TokenManager;
import com.nespresso.openapi.dto.ConfirmEmailRequest;
import com.nespresso.openapi.dto.UserRegistrationRequest;
import com.nespresso.openapi.dto.UserRegistrationResponse;
import com.nespresso.security.api.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailTokenConformer {

    private final UserRegistrationService userRegistrationService;
    private final TokenManager tokenManager;

    public UserRegistrationResponse confirmEmailByCode(final ConfirmEmailRequest confirmEmailRequest) {
        UserRegistrationRequest userRegistrationRequest = tokenManager.validateToken(confirmEmailRequest);
        return userRegistrationService.register(userRegistrationRequest);
    }

    public void confirmResetPasswordEmailByCode(final ConfirmEmailRequest confirmEmailRequest) {
        tokenManager.validateToken(confirmEmailRequest);
    }
}
