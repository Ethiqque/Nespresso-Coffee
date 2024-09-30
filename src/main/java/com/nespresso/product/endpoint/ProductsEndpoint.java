package com.nespresso.product.endpoint;

import com.nespresso.openapi.dto.BrandsDto;
import com.nespresso.openapi.dto.ProductIdsDto;
import com.nespresso.openapi.dto.ProductInfoDto;
import com.nespresso.openapi.dto.ProductListWithPaginationInfoDto;
import com.nespresso.openapi.dto.SellersDto;
import com.nespresso.openapi.product.review.api.ProductApi;
import com.nespresso.product.api.ProductsProvider;
import com.nespresso.product.api.SingleProductProvider;
import com.nespresso.product.validator.GetProductsRequestValidator;
import com.nespresso.product.api.PageableProductsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nespresso.common.util.Utils.createPageableObject;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = ProductsEndpoint.PRODUCTS_URL)
public class ProductsEndpoint implements ProductApi {

    public static final String PRODUCTS_URL = "/api/v1/products";

    private final ProductsProvider productsProvider;
    private final PageableProductsProvider pageableProductsProvider;
    private final GetProductsRequestValidator getProductsRequestValidator;
    private final SingleProductProvider singleProductProvider;

    @Override
    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfoDto> getProductById(@PathVariable final UUID productId) {
        log.info("Received the request to get the product with productId - {}.", productId);
        ProductInfoDto product = singleProductProvider.getProductById(productId);
        log.info("The product with productId: {} was retrieved successfully", productId);
        return ResponseEntity.ok()
                .body(product);
    }

    @Override
    @GetMapping
    public ResponseEntity<ProductListWithPaginationInfoDto> getProducts(@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
                                                                        @RequestParam(name = "size", defaultValue = "50") Integer pageSize,
                                                                        @RequestParam(name = "sort_attribute", defaultValue = "name") String sortAttribute,
                                                                        @RequestParam(name = "sort_direction", defaultValue = "desc") String sortDirection,
                                                                        @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
                                                                        @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
                                                                        @RequestParam(name = "minimum_average_rating", required = false) Integer minimumAverageRating,
                                                                        @RequestParam(name = "brand_names", required = false) List<String> brandNames,
                                                                        @RequestParam(name = "seller_names", required = false) List<String> sellersNames) {
        log.info("Received the request to get products with these pagination and sorting attributes: " +
                "page - {}, size - {}, sort_attribute - {}, sort_direction - {}", pageNumber, pageSize, sortAttribute, sortDirection);
        getProductsRequestValidator.validate(pageNumber, pageSize, sortAttribute, sortDirection, minPrice, maxPrice, minimumAverageRating, brandNames, sellersNames);
        Pageable pageable = createPageableObject(pageNumber, pageSize, sortAttribute, sortDirection);
        ProductListWithPaginationInfoDto productPaginationDto = pageableProductsProvider.getProducts(pageable, minPrice, maxPrice, minimumAverageRating, brandNames, sellersNames);
        log.info("Products were retrieved successfully with these pagination and sorting attributes: " +
                "page - {}, size - {}, sort_attribute - {}, sort_direction - {}", pageNumber, pageSize, sortAttribute, sortDirection);
        return ResponseEntity.ok()
                .body(productPaginationDto);
    }

    @Override
    @PostMapping("/ids")
    public ResponseEntity<List<ProductInfoDto>> getProductsByIds(@RequestBody final ProductIdsDto productIdsDto) {
        List<UUID> productIds = productIdsDto.getProductIds();
        var stringIDs = productIds.stream().map(UUID::toString).collect(Collectors.joining(", "));
        log.info("Received the request to get the products with productIds - {}.", stringIDs);
        List<ProductInfoDto> products = productsProvider.getProducts(productIds);
        log.info("Products with productIds: {} was retrieved successfully", stringIDs);
        return ResponseEntity.ok()
                .body(products);
    }

    @Override
    @GetMapping("/sellers")
    public ResponseEntity<SellersDto> getAllSellers() {
        log.info("Received the request to get a list of all hardcoded sellers of products");
        SellersDto sellersDto = new SellersDto(List.of("JavaBeanCoffee", "FreshCup", "BrewedBliss", "EspressoEmporium", "MorningMug", "CoffeeCorner", "CuppaCafe", "BeanBrewers"));
        log.info("The list of all hardcoded sellers of products was retrieved successfully");
        return ResponseEntity.ok()
                .body(sellersDto);
    }

    @Override
    @GetMapping("/brands")
    public ResponseEntity<BrandsDto> getAllBrands() {
        log.info("Received the request to get a list of all hardcoded brands of products");
        BrandsDto brandsDto = new BrandsDto(List.of("Folgers", "Illy", "Dunkin-Donuts", "Nescafe", "Lavazza", "Peets-Coffee", "Starbucks"));
        log.info("The list of all hardcoded brands of products was retrieved successfully");
        return ResponseEntity.ok()
                .body(brandsDto);
    }
}