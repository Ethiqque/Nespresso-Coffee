package com.nespresso.payment.api.scenario;

import com.stripe.model.checkout.Session;

public interface SessionScenarioHandler {

    void handle(final Session session);
}
