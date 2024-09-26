package com.nespresso.review.api;

import com.nespresso.openapi.dto.ProductReviewRatingStats;
import com.nespresso.openapi.dto.RatingMap;
import com.nespresso.review.converter.ProductReviewDtoConverter;
import com.nespresso.review.dto.ProductRatingCount;
import com.nespresso.review.repository.ProductReviewRepository;
import com.nespresso.review.validator.ProductReviewValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReviewsStatisticsProvider {

    private final ProductReviewRepository reviewRepository;
    private final ProductReviewDtoConverter productReviewDtoConverter;
    private final ProductReviewValidator productReviewValidator;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ProductReviewRatingStats get(final UUID productId) {
        productReviewValidator.validateProductExists(productId);

        String formattedAvgRating = getFormattedAverageProductRating(productId);
        Integer reviewsCount = reviewRepository.getReviewCountProductById(productId);
        RatingMap productRatingMap = getProductRatingMap(productId);

        return new ProductReviewRatingStats(productId, formattedAvgRating, reviewsCount, productRatingMap);
    }

    private String getFormattedAverageProductRating(UUID productId) {
        Double avgRating = reviewRepository.getAvgRatingByProductId(productId);
        if (avgRating == null) {
            avgRating = 0.0;
        }
        return String.format(Locale.US, "%.1f", avgRating);
    }

    private RatingMap getProductRatingMap(UUID productId) {
        List<ProductRatingCount> productRatingCountPairs = reviewRepository.getRatingsMapByProductId(productId);
        if (productRatingCountPairs == null) {
            return new RatingMap(0, 0, 0, 0, 0);
        }
        return productReviewDtoConverter.convertToProductRatingMap(productRatingCountPairs);
    }
}
