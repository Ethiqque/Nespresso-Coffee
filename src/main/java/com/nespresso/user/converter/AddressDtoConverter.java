package com.nespresso.user.converter;

import com.nespresso.openapi.dto.AddressDto;
import com.nespresso.user.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressDtoConverter {

    @Named("toAddressDto")
    AddressDto toDto(final Address entity);

    @Named("toAddress")
    Address toEntity(final AddressDto dto);
}
