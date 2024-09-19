package com.nespresso.security.api;

import com.nespresso.security.entity.LoginAttemptEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginAttemptFactory {

    public LoginAttemptEntity createInitialFailedLoggedAttemptEntity(String userEmail) {
        int attempts = 0;
        LocalDateTime lastModified = LocalDateTime.now();
        boolean isUserLocked = false;
        return new LoginAttemptEntity(null, userEmail, attempts, null, isUserLocked, lastModified);
    }
}
