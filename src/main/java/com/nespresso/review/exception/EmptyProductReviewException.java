package com.nespresso.review.exception;

public class EmptyProductReviewException extends RuntimeException {
    public EmptyProductReviewException() {
        super("Product's review is empty");
    }
}
