package com.nespresso.payment.api;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.nespresso.payment.api.scenario.SessionScenarioHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * Stripe Webhook sends <a href="https://docs.stripe.com/api/events/types">various events</a>,
 * So far we're interested only in "checkout.session.expired" and "checkout.session.completed"
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WebhookEventHandler {

    private final Map<String, SessionScenarioHandler> handlers;

    public void handlePaymentEvent(final Event stripePaymentEvent, final Session stripeSession) {

        if (!Objects.nonNull(stripeSession) || !Objects.nonNull(stripePaymentEvent)) {
            return;
        }
        handlers.get(stripePaymentEvent.getType())
                .handle(stripeSession);
    }
}
