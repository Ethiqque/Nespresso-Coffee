package com.nespresso.security.api;

import com.nespresso.openapi.dto.UserAuthenticationRequest;
import com.nespresso.openapi.dto.UserAuthenticationResponse;
import com.nespresso.security.exception.UserAccountLockedException;
import com.nespresso.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private static final int USER_ACCOUNT_LOCKOUT_DURATION_MINUTES = 60;
    private static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "Invalid credentials for user's account with email = '%s'";

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final LoginFailureHandler loginFailureHandler;
    private final ResetLoginAttemptsService resetLoginAttemptsService;

    public UserAuthenticationResponse authenticate(final UserAuthenticationRequest request) {
        String userEmail = request.getEmail();
        String userPassword = request.getPassword();

        log.info("Authenticating user with email = '{}'", userEmail);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, userPassword)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
            String jwtToken = jwtTokenProvider.generateToken(userDetails);
            log.info("Generated JWT token for user with email = '{}'", request.getEmail());

            resetLoginAttemptsService.reset(userEmail);

            UserAuthenticationResponse response = new UserAuthenticationResponse();
            response.setToken(jwtToken);
            response.setRefreshToken(jwtRefreshToken);
            return response;

        } catch (UsernameNotFoundException exception) {
            log.warn("User with the provided email='{}' does not exist", userEmail, exception);
            throw new UsernameNotFoundException(String.format(INVALID_CREDENTIALS_ERROR_MESSAGE, userEmail), exception);
        } catch (BadCredentialsException exception) {
            log.warn("Invalid credentials for user's account with email = '{}'", userEmail, exception);
            loginFailureHandler.handle(userEmail);
            throw new BadCredentialsException(String.format(INVALID_CREDENTIALS_ERROR_MESSAGE, userEmail), exception);

        } catch (LockedException exception) {
            log.warn("User's account with email = '{}' is locked", userEmail, exception);
            throw new UserAccountLockedException(userEmail, USER_ACCOUNT_LOCKOUT_DURATION_MINUTES);

        } catch (Exception exception) {
            log.error("Error occurred during authentication", exception);
            throw exception;
        }
    }

    public UserAuthenticationResponse authenticate(final UserDetails userDetails, String userEmail) {
        try {
            String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
            String jwtToken = jwtTokenProvider.generateToken(userDetails);
            log.info("Generated JWT token for user with email = '{}'", userEmail);

            resetLoginAttemptsService.reset(userEmail);

            UserAuthenticationResponse response = new UserAuthenticationResponse();
            response.setToken(jwtToken);
            response.setRefreshToken(jwtRefreshToken);
            return response;
        } catch (BadCredentialsException exception) {
            log.warn("Invalid credentials for user's account with email = '{}'", userEmail, exception);
            loginFailureHandler.handle(userEmail);
            throw new BadCredentialsException(String.format(INVALID_CREDENTIALS_ERROR_MESSAGE, userEmail), exception);

        } catch (LockedException exception) {
            log.warn("User's account with email = '{}' is locked", userEmail, exception);
            throw new UserAccountLockedException(userEmail, USER_ACCOUNT_LOCKOUT_DURATION_MINUTES);

        } catch (Exception exception) {
            log.error("Error occurred during authentication", exception);
            throw exception;
        }
    }
}
