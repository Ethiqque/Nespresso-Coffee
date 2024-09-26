package com.nespresso.cart.converter;

import com.nespresso.openapi.dto.ShoppingCartItemDto;
import com.nespresso.cart.entity.ShoppingCartItem;
import com.nespresso.product.converter.ProductInfoDtoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ProductInfoDtoConverter.class)
public interface ShoppingCartItemDtoConverter {

    @Named("toShoppingCartItemDto")
    @Mapping(target = "productInfo", source = "productInfo", qualifiedByName = {"toProductInfoDto"})
    ShoppingCartItemDto toDto(final ShoppingCartItem entity);
}
