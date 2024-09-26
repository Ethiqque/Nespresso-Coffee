package com.nespresso.favorite.api;

import com.nespresso.favorite.converter.FavoriteListDtoConverter;
import com.nespresso.favorite.dto.FavoriteListDto;
import com.nespresso.favorite.entity.FavoriteListEntity;
import com.nespresso.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FavoriteListProvider {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteListDtoConverter favoriteListDtoConverter;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public FavoriteListEntity getFavoriteListEntity(final UUID userId) {
        return favoriteRepository.findByUserId(userId)
                .orElseGet(() -> createNewFavoriteList(userId));
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public FavoriteListDto getFavoriteListDto(final UUID userId) {
        FavoriteListEntity favoriteListEntity = favoriteRepository.findByUserId(userId)
                .orElseGet(() -> createNewFavoriteList(userId));
        return favoriteListDtoConverter.toDto(favoriteListEntity);
    }

    private FavoriteListEntity createNewFavoriteList(UUID userId) {
        return FavoriteListEntity.builder()
                .userId(userId)
                .favoriteItems(ConcurrentHashMap.newKeySet())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}
