package com.nespresso.payment.api;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for parsing payment event to session object.
 */
@Slf4j
@Service
public class WebhookEventParser {

    public Session parseEventToSession(final Event stripePaymentEvent) {
        log.info("Trying to parse StripePaymentEvent = '{}'", stripePaymentEvent.getType());

        return stripePaymentEvent.getDataObjectDeserializer()
                .getObject()
                .filter(Session.class::isInstance)
                .map(Session.class::cast)
                .orElseGet(() -> {
                    log.info("Event = '{}' is not related to Stripe session, skipping", stripePaymentEvent.getType());
                    return null;
                });
    }
}
