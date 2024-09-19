package com.nespresso.payment.exception;

public class StripeSessionRetrievalException extends RuntimeException {

    public StripeSessionRetrievalException(final String message,
                                           final String sessionId) {
        super(String.format("Error retrieving Stripe session with id = '%s'. Error message = '%s'", sessionId, message));
    }
}
