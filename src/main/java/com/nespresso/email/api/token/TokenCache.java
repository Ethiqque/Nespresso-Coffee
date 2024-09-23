package com.zufar.icedlatte.email.api.token;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zufar.icedlatte.email.exception.IncorrectTokenException;

import com.zufar.icedlatte.openapi.dto.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenCache {

    private final Cache<String, UserRegistrationRequest> tokenCache;

    public TokenCache(@Value("${temporary-cache.time.token}") Integer expireTime) {
        this.tokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireTime, TimeUnit.MINUTES)
                .build();
    }

    public void addToken(String tokenKey, UserRegistrationRequest request) {
        tokenCache.put(tokenKey, request);
    }

    public UserRegistrationRequest getToken(String tokenKey) {
        UserRegistrationRequest userRegistrationRequest = tokenCache.getIfPresent(tokenKey);
        if (userRegistrationRequest == null) {
            throw new IncorrectTokenException();
        }
        return userRegistrationRequest;
    }

    public void removeToken(String tokenKey) {
        tokenCache.invalidate(tokenKey);
    }
}
