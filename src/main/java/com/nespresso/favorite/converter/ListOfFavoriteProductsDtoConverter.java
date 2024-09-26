package com.nespresso.favorite.converter;

import com.nespresso.favorite.dto.FavoriteItemDto;
import com.nespresso.favorite.dto.FavoriteListDto;
import com.nespresso.openapi.dto.ProductInfoDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.FIELD)
public interface ListOfFavoriteProductsDtoConverter {

    @Mapping(target = "products", source = "favoriteItems", qualifiedByName = "toListProductInfoDto")
    ListOfFavoriteProductsDto toListProductDto(FavoriteListDto favoriteList);

    @Named("toListProductInfoDto")
    default List<ProductInfoDto> toProductInfoDto(final Set<FavoriteItemDto> favoriteItems) {
        return favoriteItems.stream()
                .map(FavoriteItemDto::productInfo)
                .toList();
    }
}