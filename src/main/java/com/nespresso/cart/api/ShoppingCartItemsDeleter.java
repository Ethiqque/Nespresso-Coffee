package com.nespresso.cart.api;

import com.nespresso.openapi.dto.DeleteItemsFromShoppingCartRequest;
import com.nespresso.openapi.dto.ShoppingCartDto;
import com.nespresso.cart.repository.ShoppingCartItemRepository;
import com.nespresso.security.api.SecurityPrincipalProvider;
import com.nespresso.openapi.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartItemsDeleter {

    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartProvider shoppingCartProvider;
    private final SecurityPrincipalProvider securityPrincipalProvider;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ShoppingCartDto delete(final DeleteItemsFromShoppingCartRequest request) {
        List<UUID> itemIds = request.getShoppingCartItemIds();
        shoppingCartItemRepository.deleteAllByIdInBatch(itemIds);

        UserDto userDto = securityPrincipalProvider.get();
        UUID userId = userDto.getId();

        return shoppingCartProvider.getByUserId(userId);
    }
}
