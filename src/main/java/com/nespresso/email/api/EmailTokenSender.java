package com.nespresso.email.api;

import com.nespresso.email.api.token.TokenManager;
import com.nespresso.email.sender.AuthTokenEmailConfirmation;
import com.nespresso.openapi.dto.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailTokenSender {

    private final AuthTokenEmailConfirmation emailConfirmation;
    private final TokenManager tokenManager;

    public void sendEmailVerificationCode(final UserRegistrationRequest request) {
        String token = tokenManager.generateToken(request);
        emailConfirmation.sendTemporaryCode(request.getEmail(), token);
    }
}
