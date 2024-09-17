package com.nespresso.user.api;

import com.nespresso.openapi.dto.AddressDto;
import com.nespresso.openapi.dto.UpdateUserAccountRequest;
import com.nespresso.openapi.dto.UserDto;
import com.nespresso.security.api.SecurityPrincipalProvider;
import com.nespresso.user.converter.AddressDtoConverter;
import com.nespresso.user.converter.UserDtoConverter;
import com.nespresso.user.entity.Address;
import com.nespresso.user.entity.UserEntity;
import com.nespresso.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserOperationPerformer {

    private final SingleUserProvider singleUserProvider;
    private final UserRepository userCrudRepository;
    private final UserDtoConverter userDtoConverter;
    private final AddressDtoConverter addressDtoConverter;
    private final SecurityPrincipalProvider securityPrincipalProvider;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public UserDto updateUser(final UpdateUserAccountRequest updateUserAccountRequest) {
        UUID userId = securityPrincipalProvider.getUserId();

        UserEntity userEntity = singleUserProvider.getUserEntityById(userId);

        AddressDto addressDto = updateUserAccountRequest.getAddress();
        Address addressEntity = addressDtoConverter.toEntity(addressDto);

        userEntity.setFirstName(updateUserAccountRequest.getFirstName());
        userEntity.setLastName(updateUserAccountRequest.getLastName());
        userEntity.setBirthDate(updateUserAccountRequest.getBirthDate());
        userEntity.setPhoneNumber(updateUserAccountRequest.getPhoneNumber());
        userEntity.setAddress(addressEntity);

        UserEntity userEntityWithId = userCrudRepository.save(userEntity);
        return userDtoConverter.toDto(userEntityWithId);
    }
}
