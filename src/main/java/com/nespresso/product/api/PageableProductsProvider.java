package com.nespresso.product.api;

import com.nespresso.openapi.dto.ProductInfoDto;
import com.nespresso.openapi.dto.ProductListWithPaginationInfoDto;
import com.nespresso.product.converter.ProductInfoDtoConverter;
import com.nespresso.product.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class PageableProductsProvider {

    private final ProductInfoRepository productInfoRepository;
    private final ProductInfoDtoConverter productInfoDtoConverter;
    private final ProductUpdater productUpdater;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ProductListWithPaginationInfoDto getProducts(final Pageable pageable,
                                                        final BigDecimal minPrice,
                                                        final BigDecimal maxPrice,
                                                        final Integer minimumAverageRating,
                                                        final List<String> brandNames,
                                                        final List<String> sellerNames) {
        BigDecimal minimumAverageRatingValue = minimumAverageRating == null ? null : BigDecimal.valueOf(minimumAverageRating);

        Page<ProductInfoDto> productsWithPageInfo = productInfoRepository
                .findAllProducts(minPrice, maxPrice, minimumAverageRatingValue, brandNames, sellerNames, pageable)
                .map(productInfoDtoConverter::toDto)
                .map(productUpdater::update);

        return productInfoDtoConverter.toProductPaginationDto(productsWithPageInfo);
    }
}