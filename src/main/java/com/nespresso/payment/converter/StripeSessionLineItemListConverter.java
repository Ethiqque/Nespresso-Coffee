package com.nespresso.payment.converter;

import com.stripe.param.checkout.SessionCreateParams;
import com.nespresso.openapi.dto.ShoppingCartItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = BigDecimal.class)
public interface StripeSessionLineItemListConverter {

    List<SessionCreateParams.LineItem> toLineItems(List<ShoppingCartItemDto> shoppingCartItems);

    @Mapping(target = "priceData.unitAmount", source = "productInfo.price", qualifiedByName = "toStripeUnitAmount")
    @Mapping(target = "priceData.currency", constant = "USD")
    @Mapping(target = "priceData.productData.name", source = "productInfo.name")
    @Mapping(target = "quantity", source = "productQuantity")
    SessionCreateParams.LineItem toLineItem(ShoppingCartItemDto shoppingCartItem);

    @Named("toStripeUnitAmount")
    default Long toStripeUnitAmount(final BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(100)).longValue();
    }
}
