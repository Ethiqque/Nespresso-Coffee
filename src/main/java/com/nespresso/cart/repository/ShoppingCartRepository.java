package com.nespresso.cart.repository;

import com.nespresso.cart.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"items", "items.productInfo"})
    ShoppingCart findShoppingCartByUserId(UUID userId);

    @Modifying(flushAutomatically = true)
    void deleteByUserId(UUID userId);
}