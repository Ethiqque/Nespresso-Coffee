package com.nespresso.payment.api;

import com.stripe.model.checkout.Session;
import com.nespresso.openapi.dto.PaymentConfirmationEmail;
import com.nespresso.payment.api.scenario.SessionCompletedScenarioHandler;
import com.nespresso.payment.exception.StripeSessionIsNotComplete;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This class has the same aim as WebhookEventProcessor,
 * but it's supposed to be used for processing request from frontend which is sent after
 * Stripe redirects to the specified URL.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedirectEventProcessor {

    private static final String SESSION_COMPLETE = "complete";
    private final StripeSessionProvider stripeSessionProvider;
    private final SessionCompletedScenarioHandler sessionCompletedScenarioHandler;

    public PaymentConfirmationEmail processPaymentEvent(final String sessionId) {
        log.info("Started to process Stripe payment event after redirect");
        Session session = stripeSessionProvider.get(sessionId);
        String status = session.getStatus();
        if (!SESSION_COMPLETE.equals(status)) {
            log.error("Payment event cannot be processed, because session is not completed, session status: {}", status);
            throw new StripeSessionIsNotComplete(sessionId, status);
        }
        sessionCompletedScenarioHandler.handle(session);
        log.info("Finished to process Stripe payment event after redirect");
        var response = new PaymentConfirmationEmail();
        response.customerEmail(session.getCustomerEmail());
        return response;
    }
}
