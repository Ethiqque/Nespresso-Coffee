package com.nespresso.cart.converter;

import com.nespresso.cart.api.ItemsTotalPriceCalculator;
import com.nespresso.cart.entity.ShoppingCart;
import com.nespresso.openapi.dto.ShoppingCartDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ShoppingCartItemDtoConverter.class, ItemsTotalPriceCalculator.class})
public interface ShoppingCartDtoConverter {

    @Mapping(target = "items", source = "entity.items", qualifiedByName = {"toShoppingCartItemDto"})
    @Mapping(target = "itemsTotalPrice", source = "entity.items", qualifiedByName = {"toItemsTotalPrice"})
    ShoppingCartDto toDto(final ShoppingCart entity);
}
