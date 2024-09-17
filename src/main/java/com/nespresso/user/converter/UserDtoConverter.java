package com.nespresso.user.converter;

import com.nespresso.openapi.dto.UpdateUserAccountRequest;
import com.nespresso.openapi.dto.UserDto;
import com.nespresso.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AddressDtoConverter.class)
public interface UserDtoConverter {

    @Mapping(target = "address", source = "entity.address", qualifiedByName = "toAddressDto")
    UserDto toDto(final UserEntity entity);

    @Mapping(target = "address", source = "updateUserAccountRequest.address", qualifiedByName = "toAddress")
    UserEntity toEntity(final UpdateUserAccountRequest updateUserAccountRequest);
}
