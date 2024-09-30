package com.nespresso.security.api;

import com.nespresso.openapi.dto.UserDto;
import com.nespresso.user.converter.UserDtoConverter;
import com.nespresso.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityPrincipalProvider {

    private final UserDtoConverter userDtoConverter;

    public SecurityPrincipalProvider(UserDtoConverter userDtoConverter) {
        this.userDtoConverter = userDtoConverter;
    }

    public UserDto get() {
        UserEntity userEntity = (UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDtoConverter.toDto(userEntity);
    }

    public UUID getUserId() {
        return get().getId();
    }
}
