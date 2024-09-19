package com.nespresso.security.converter;

import com.nespresso.openapi.dto.UserRegistrationRequest;
import com.nespresso.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegistrationDtoConverter {

    UserEntity toEntity(final UserRegistrationRequest userRegistrationRequest);
}
