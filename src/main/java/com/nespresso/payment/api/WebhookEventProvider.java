package com.nespresso.payment.api;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.nespresso.payment.exception.PaymentEventProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for payment event (stripe object) retrieval using Stripe API.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WebhookEventProvider {

    @Value("${stripe.webhook-secret}")
    private String webHookSecret;

    public Event createPaymentEvent(final String paymentPayload,
                                    final String stripeSignatureHeader) {
        try {
            return Webhook.constructEvent(paymentPayload, stripeSignatureHeader, webHookSecret);
        } catch (SignatureVerificationException ex) {
            log.error("Error during Stripe payment event creating", ex);
            throw new PaymentEventProcessingException(stripeSignatureHeader);
        }
    }
}
