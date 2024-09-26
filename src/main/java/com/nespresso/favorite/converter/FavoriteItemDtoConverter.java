package com.nespresso.favorite.converter;

import com.nespresso.favorite.dto.FavoriteItemDto;
import com.nespresso.favorite.entity.FavoriteItemEntity;
import com.nespresso.product.converter.ProductInfoDtoConverter;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ProductInfoDtoConverter.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,  injectionStrategy = InjectionStrategy.FIELD)
public interface FavoriteItemDtoConverter {

    @Mapping(target = "productInfo", source = "productInfo")
    @Mapping(target = "productInfo.dateAdded", source = "productInfo.dateAdded", qualifiedByName = "localToOffsetDate")
    FavoriteItemDto toDto(@Context final ProductInfoDtoConverter productInfoDtoConverter,
                          final FavoriteItemEntity favoriteItemEntity);

}