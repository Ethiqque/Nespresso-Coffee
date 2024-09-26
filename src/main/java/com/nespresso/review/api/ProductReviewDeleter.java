package com.nespresso.review.api;

import com.nespresso.product.repository.ProductInfoRepository;
import com.nespresso.review.repository.ProductReviewRepository;
import com.nespresso.review.validator.ProductReviewValidator;
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
public class ProductReviewDeleter {

    private final ProductReviewRepository reviewRepository;
    private final ProductReviewValidator productReviewValidator;
    private final ProductInfoRepository productInfoRepository;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void delete(final UUID productId,
                       final UUID productReviewId) {
        productReviewValidator.validateProductReviewDeletionAllowed(productReviewId);
        productReviewValidator.validateProductExists(productId);

        reviewRepository.deleteById(productReviewId);

        productInfoRepository.updateAverageRating(productId);
        productInfoRepository.updateReviewsCount(productId);
    }
}
