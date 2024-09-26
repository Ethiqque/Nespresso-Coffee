package com.nespresso.review.exception;

import com.nespresso.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class ProductNotFoundForReviewException extends ResourceNotFoundException {

    public ProductNotFoundForReviewException(UUID productId) {
        super(String.format("Product with productId = '%s' was not found. " +
                "Product's review operations (update, delete, provide) are not possible.", productId));
    }
}
