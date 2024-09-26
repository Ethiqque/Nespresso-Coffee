package com.nespresso.product.api;

import com.nespresso.openapi.dto.ProductInfoDto;
import com.nespresso.product.converter.ProductInfoDtoConverter;
import com.nespresso.product.exception.ProductNotFoundException;
import com.nespresso.product.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsProvider {

    private final ProductInfoRepository productInfoRepository;
    private final ProductInfoDtoConverter productInfoDtoConverter;
    private final ProductUpdater productUpdater;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<ProductInfoDto> getProducts(final List<UUID> uuids) {
        var products = productInfoRepository.findAllById(uuids);
        var result = products.stream()
                .map(productInfoDtoConverter::toDto)
                .map(productUpdater::update)
                .toList();

        if (result.size() == uuids.size()) {
            return result;
        }

        uuids.removeAll(result.stream().map(ProductInfoDto::getId).collect(Collectors.toSet()));
        log.error("Products with ids = {} are not found.", String.join(", ",
                uuids.stream().map(UUID::toString).collect(Collectors.joining())));
        throw new ProductNotFoundException(uuids);
    }
}
