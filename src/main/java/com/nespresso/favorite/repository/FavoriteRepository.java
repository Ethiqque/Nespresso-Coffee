package com.nespresso.favorite.repository;

import com.nespresso.favorite.entity.FavoriteListEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteListEntity, UUID> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"favoriteItems", "favoriteItems.favoriteListEntity"})
    Optional<FavoriteListEntity> findByUserId(UUID userId);
}