package com.nespresso.payment.api.scenario;

import com.stripe.model.checkout.Session;
import com.nespresso.email.sender.PaymentEmailConfirmation;
import com.nespresso.order.api.OrderCreator;
import com.nespresso.payment.enums.StripeSessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@RequiredArgsConstructor
@Service(StripeSessionConstants.SESSION_IS_COMPLETED)
public class SessionCompletedScenarioHandler implements SessionScenarioHandler {

    private final PaymentEmailConfirmation paymentEmailConfirmation;
    private final OrderCreator orderCreator;

    public void handle(Session stripeSession) {
        boolean orderWasCreated = orderCreator.createOrderAndDeleteCart(stripeSession);
        if (orderWasCreated) {
            paymentEmailConfirmation.send(stripeSession);
            log.info("Confirmation email was sent");
        }
    }
}
