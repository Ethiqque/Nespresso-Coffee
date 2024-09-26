package com.nespresso.favorite.converter;

import com.nespresso.favorite.dto.FavoriteItemDto;
import com.nespresso.favorite.dto.FavoriteListDto;
import com.nespresso.favorite.entity.FavoriteItemEntity;
import com.nespresso.favorite.entity.FavoriteListEntity;
import com.nespresso.openapi.dto.ProductInfoDto;
import com.nespresso.product.entity.ProductInfo;
import com.nespresso.user.entity.UserEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = FavoriteItemDtoConverter.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.FIELD)
public interface FavoriteListDtoConverter {
    @Mapping(target = "favoriteItems", source = "favoriteItems", qualifiedByName = "mapFavoriteItems")
    FavoriteListDto toDto(final FavoriteListEntity favoriteListEntity);

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "dateAdded", source = "dateAdded", qualifiedByName = "localToOffsetDate")
    ProductInfoDto convertProductInfoDto(ProductInfo productInfo);

    @Named("toUserId")
    default UUID convertToUserId(UserEntity user) {
        Optional<UserEntity> userOptional = Optional.ofNullable(user);
        Optional<UUID> userIdOptional = userOptional.map(UserEntity::getId);
        return userIdOptional.orElse(null);
    }

    @Named("mapFavoriteItems")
    default FavoriteItemDto toFavoriteItemDto(FavoriteItemEntity itemEntity) {
        UUID favoriteItemEntityId = itemEntity.getId();
        ProductInfo productInfo = itemEntity.getProductInfo();

        ProductInfoDto productInfoDto = convertProductInfoDto(productInfo);

        return new FavoriteItemDto(favoriteItemEntityId, productInfoDto);
    }

    @Named("localToOffsetDate")
    default OffsetDateTime offsetToLocalDate(LocalDateTime value) {
        if (value != null) {
            return OffsetDateTime.of(value, ZoneOffset.UTC);
        }
        return null;
    }

}