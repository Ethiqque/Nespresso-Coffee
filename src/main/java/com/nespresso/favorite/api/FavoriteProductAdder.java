package com.nespresso.favorite.api;

import com.nespresso.favorite.converter.FavoriteListDtoConverter;
import com.nespresso.favorite.dto.FavoriteListDto;
import com.nespresso.favorite.entity.FavoriteItemEntity;
import com.nespresso.favorite.entity.FavoriteListEntity;
import com.nespresso.favorite.repository.FavoriteRepository;
import com.nespresso.openapi.dto.ListOfFavoriteProducts;
import com.nespresso.product.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteProductAdder {

    private final FavoriteRepository favoriteRepository;
    private final ProductInfoRepository productInfoRepository;
    private final FavoriteListDtoConverter favoriteListDtoConverter;
    private final FavoriteListProvider favoriteListProvider;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public FavoriteListDto add(final ListOfFavoriteProducts listOfFavoriteProducts, final UUID userId) {
        FavoriteListEntity favoriteListEntity = favoriteListProvider.getFavoriteListEntity(userId);

        Set<UUID> existingFavoriteProductIds = extractFavoriteProductIds(favoriteListEntity);
        Set<UUID> newFavoriteItemIds = filterNewFavoriteProductIds(listOfFavoriteProducts, existingFavoriteProductIds);

        Set<FavoriteItemEntity> newFavoriteItems = createFavoriteItems(newFavoriteItemIds, favoriteListEntity);
        favoriteListEntity.getFavoriteItems().addAll(newFavoriteItems);

        FavoriteListEntity updatedFavoriteListEntity = favoriteRepository.save(favoriteListEntity);
        return favoriteListDtoConverter.toDto(updatedFavoriteListEntity);
    }

    private Set<UUID> extractFavoriteProductIds(FavoriteListEntity favoriteListEntity) {
        return favoriteListEntity.getFavoriteItems().stream()
                .map(item -> item.getProductInfo().getProductId())
                .collect(Collectors.toSet());
    }

    private Set<UUID> filterNewFavoriteProductIds(ListOfFavoriteProducts listOfFavoriteProducts, Set<UUID> existingIds) {
        return listOfFavoriteProducts.getProductIds().stream()
                .filter(productId -> !existingIds.contains(productId))
                .collect(Collectors.toSet());
    }

    private Set<FavoriteItemEntity> createFavoriteItems(Set<UUID> productIds, FavoriteListEntity favoriteListEntity) {
        return productInfoRepository.findAllById(productIds).stream()
                .map(productInfo -> FavoriteItemEntity.builder()
                        .favoriteListEntity(favoriteListEntity)
                        .productInfo(productInfo)
                        .build())
                .collect(Collectors.toSet());
    }
}
