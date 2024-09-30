package com.nespresso.payment.endpoint;

import com.nespresso.openapi.dto.PaymentConfirmationEmail;
import com.nespresso.openapi.dto.SessionWithClientSecretDto;
import com.nespresso.payment.api.PaymentProcessor;
import com.nespresso.openapi.payment.api.PaymentApi;
import com.nespresso.payment.api.RedirectEventProcessor;
import com.nespresso.payment.api.WebhookEventProcessor;
import com.nespresso.payment.exception.StripeSessionRetrievalException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = PaymentEndpoint.PAYMENT_URL)
public class PaymentEndpoint implements PaymentApi {

    public static final String PAYMENT_URL = "/api/v1/payment";

    private final PaymentProcessor paymentProcessor;
    private final WebhookEventProcessor webhookEventProcessor;
    private final RedirectEventProcessor redirectEventProcessor;

    @Hidden
    @GetMapping
    public ResponseEntity<SessionWithClientSecretDto> processPayment(final HttpServletRequest request) {
        log.info("Received request to process payment");
        var processPaymentResponse = paymentProcessor.processPayment(request);
        log.info("Payment session was created successfully");
        return ResponseEntity.ok()
                .body(processPaymentResponse);
    }

    /**
     * Handles various requests which are sent by Stripe
     *
     * @param stripeSignatureHeader - Stripe Signature which is used for authorization
     * @param paymentPayload - serialized event: Stripe sends many events, we're interested only in Session events
     */
    @Hidden
    @PostMapping("/stripe/webhook")
    public ResponseEntity<Void> processStripeWebhook(@RequestHeader("Stripe-Signature") final String stripeSignatureHeader,
                                                     @RequestBody final String paymentPayload){
        log.info("Received Stripe payment webhook");
        webhookEventProcessor.processPaymentEvent(paymentPayload, stripeSignatureHeader);
        log.info("Finished processing Stripe payment webhook");
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/order")
    public ResponseEntity<PaymentConfirmationEmail> processRedirectEvent(@RequestParam final String sessionId) throws StripeSessionRetrievalException {
        log.info("Received request to create order after redirect, session id {}", sessionId);
        var paymentConfirmationEmail = redirectEventProcessor.processPaymentEvent(sessionId);
        log.info("Finished creating order after redirect");
        return ResponseEntity.ok().body(paymentConfirmationEmail);
    }
}