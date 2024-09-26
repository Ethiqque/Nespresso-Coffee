package com.nespresso.review.api;

import com.nespresso.review.entity.ProductReview;
import com.nespresso.review.exception.ProductReviewNotFoundException;
import com.nespresso.review.repository.ProductReviewRepository;
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
public class ProductReviewProvider {

    private final ProductReviewRepository reviewRepository;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ProductReview getReviewEntityById(final UUID productReviewId) {
        return reviewRepository.findById(productReviewId)
                .orElseThrow(() -> {
                    log.warn("Failed to get the review entity with productReviewId = '{}'", productReviewId);
                    return new ProductReviewNotFoundException(productReviewId);
                });
    }
}
