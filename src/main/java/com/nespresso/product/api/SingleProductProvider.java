package com.nespresso.product.api;

import com.nespresso.openapi.dto.ProductInfoDto;
import com.nespresso.product.converter.ProductInfoDtoConverter;
import com.nespresso.product.entity.ProductInfo;
import com.nespresso.product.exception.ProductNotFoundException;
import com.nespresso.product.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SingleProductProvider {

    private final ProductInfoRepository productInfoRepository;
    private final ProductInfoDtoConverter productInfoDtoConverter;
    private final ProductUpdater productUpdater;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ProductInfoDto getProductById(final UUID productId) {
        return productInfoRepository.findById(productId)
                .map(productInfoDtoConverter::toDto)
                .map(productUpdater::update)
                .orElseThrow(() -> {
                    log.error("The product with id = {} was not found.", productId);
                    return new ProductNotFoundException(productId);
                });
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ProductInfo getProductEntityById(final UUID productId) {
        return productInfoRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Failed to get the product entity: {}", productId);
                    return new ProductNotFoundException(productId);
                });
    }
}
