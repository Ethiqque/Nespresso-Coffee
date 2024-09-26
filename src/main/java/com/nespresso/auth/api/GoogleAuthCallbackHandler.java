package com.nespresso.auth.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nespresso.openapi.dto.UserAuthenticationResponse;
import com.nespresso.security.jwt.JwtTokenProvider;
import com.nespresso.user.entity.Authority;
import com.nespresso.user.entity.UserEntity;
import com.nespresso.user.entity.UserGrantedAuthority;
import com.nespresso.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthCallbackHandler {

    private final GoogleIdTokenCreator googleIdTokenCreator;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userCrudRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationResponse googleAuthCallback(String authorizationCode) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenCreator.createGoogleIdToken(authorizationCode);
        if (idToken == null) {
            log.error("Invalid ID token.");
            throw new IllegalStateException("Invalid ID token.");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = (String) payload.get("email");
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");

        if (StringUtils.isEmpty(email)) {
            log.error("Error during Google authentication callback. The user's email is empty.");
            throw new IllegalStateException("Error during Google authentication callback. The user's email is empty.");
        }

        UserEntity userEntity = userRepository
                .findByEmail(email)
                .orElseGet(() -> registerNewUser(firstName, lastName, email));

        final String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
        final String jwtToken = jwtTokenProvider.generateToken(userEntity);

        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setToken(jwtToken);
        response.setRefreshToken(jwtRefreshToken);
        return response;
    }

    private UserEntity registerNewUser(final String firstName,
                                       final String lastName,
                                       final String email) {
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);
        Set<UserGrantedAuthority> authorities = Collections.singleton(UserGrantedAuthority.builder().authority(Authority.USER).build());

        UserEntity userEntity = UserEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(encodedPassword)
                .authorities(authorities)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        return userCrudRepository.save(userEntity);
    }
}
