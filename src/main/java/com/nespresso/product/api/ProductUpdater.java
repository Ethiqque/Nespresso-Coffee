package com.nespresso.product.api;

import com.nespresso.openapi.dto.ProductInfoDto;
import com.nespresso.product.api.filestorage.ProductPictureLinkUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUpdater {

    private final ProductPictureLinkUpdater productPictureLinkUpdater;

    public ProductInfoDto update(ProductInfoDto productInfoDto) {
        productPictureLinkUpdater.update(productInfoDto);
        return productInfoDto;
    }
}
