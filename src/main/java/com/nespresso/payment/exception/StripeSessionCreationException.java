package com.nespresso.payment.exception;

public class StripeSessionCreationException extends RuntimeException {

    public StripeSessionCreationException(final String message) {
        super(String.format("Error creating Stripe session. Error message = '%s'", message));
    }
}
