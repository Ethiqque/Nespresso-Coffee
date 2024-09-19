package com.nespresso.security.api;

import com.nespresso.openapi.dto.UserRegistrationRequest;
import com.nespresso.openapi.dto.UserRegistrationResponse;
import com.nespresso.security.converter.RegistrationDtoConverter;
import com.nespresso.security.jwt.JwtTokenProvider;
import com.nespresso.user.entity.Authority;
import com.nespresso.user.entity.UserEntity;
import com.nespresso.user.entity.UserGrantedAuthority;
import com.nespresso.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private static final boolean DEFAULT_ACCOUNT_NON_EXPIRED = true;
    private static final boolean DEFAULT_ACCOUNT_NON_LOCKED = true;
    private static final boolean DEFAULT_CREDENTIALS_NON_EXPIRED = true;
    private static final boolean DEFAULT_ENABLED = true;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userCrudRepository;
    private final RegistrationDtoConverter registrationDtoConverter;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationResponse register(final UserRegistrationRequest userRegistrationRequest) {
        String encryptedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
        UserGrantedAuthority defaultUserGrantedAuthority = UserGrantedAuthority.builder().authority(Authority.USER).build();

        UserEntity newUserEntity = registrationDtoConverter.toEntity(userRegistrationRequest);
        newUserEntity.setPassword(encryptedPassword);
        newUserEntity.addAuthority(defaultUserGrantedAuthority);
        newUserEntity.setAccountNonExpired(DEFAULT_ACCOUNT_NON_EXPIRED);
        newUserEntity.setAccountNonLocked(DEFAULT_ACCOUNT_NON_LOCKED);
        newUserEntity.setCredentialsNonExpired(DEFAULT_CREDENTIALS_NON_EXPIRED);
        newUserEntity.setEnabled(DEFAULT_ENABLED);

        UserEntity userEntity = userCrudRepository.save(newUserEntity);

        final String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
        final String jwtToken = jwtTokenProvider.generateToken(userEntity);

        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setToken(jwtToken);
        response.setRefreshToken(jwtRefreshToken);
        return response;
    }
}
