package com.nespresso.favorite.dto;

import com.nespresso.openapi.dto.ProductInfoDto;

import java.util.UUID;

public record FavoriteItemDto(UUID id,
                              ProductInfoDto productInfo) {}