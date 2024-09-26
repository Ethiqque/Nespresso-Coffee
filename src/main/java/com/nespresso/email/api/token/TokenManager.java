package com.nespresso.email.api.token;

import com.nespresso.openapi.dto.ConfirmEmailRequest;
import com.nespresso.openapi.dto.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final TokenCache tokenCache;
    private final TokenGenerator tokenGenerator;
    private final TokenTimeExpirationCache tokenTimeExpirationCache;

    public String generateToken(final UserRegistrationRequest request) {
        final String email = request.getEmail();
        tokenTimeExpirationCache.validateTimeToken(email);
        final String token = tokenGenerator.nextToken();
        tokenCache.addToken(token, request);
        tokenTimeExpirationCache.manageEmailSendingRate(email);
        return token;
    }

    public UserRegistrationRequest validateToken(final ConfirmEmailRequest confirmEmailRequest) {
        final String token = confirmEmailRequest.getToken();
        tokenGenerator.tokenIsValid(token);
        return deleteTokenFromCache(token);
    }

    public UserRegistrationRequest deleteTokenFromCache(String token) {
        UserRegistrationRequest userRegistrationRequest = tokenCache.getToken(token);
        tokenCache.removeToken(token);
        tokenTimeExpirationCache.removeToken(userRegistrationRequest.getEmail());
        return userRegistrationRequest;
    }
}
