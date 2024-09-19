package com.nespresso.payment.api;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Stripe Webhook sends <a href="https://docs.stripe.com/api/events/types">various events</a>,
 * So far we're interested only in "checkout.session.expired" and "checkout.session.completed".
 * In case of "checkout.session.completed", order is created (if it wasn't created by calling ????) and cart is removed,
 * In case of "checkout.session.expired", this event is just logged.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WebhookEventProcessor {

    private final WebhookEventProvider webhookEventProvider;
    private final WebhookEventParser webhookEventParser;
    private final WebhookEventHandler webhookEventHandler;

    public void processPaymentEvent(String paymentPayload, String stripeSignatureHeader) {
        log.info("Started to process Stripe payment event after webhook");
        Event stripePaymentEvent = webhookEventProvider.createPaymentEvent(paymentPayload, stripeSignatureHeader);
        Session stripeSession = webhookEventParser.parseEventToSession(stripePaymentEvent);
        webhookEventHandler.handlePaymentEvent(stripePaymentEvent, stripeSession);
        log.info("Successfully finished to process Stripe payment event after webhook");
    }

}
