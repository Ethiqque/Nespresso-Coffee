package com.nespresso.payment.api;

import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StripeShippingOptionsProvider {

    public List<SessionCreateParams.ShippingOption> getShippingOptions() {
        var result = new ArrayList<SessionCreateParams.ShippingOption>();
        var option1 = SessionCreateParams.ShippingOption.builder()
                .setShippingRateData(
                        SessionCreateParams.ShippingOption.ShippingRateData.builder()
                                .setType(
                                        SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
                                )
                                .setFixedAmount(
                                        SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                                                .setAmount(0L)
                                                .setCurrency("usd")
                                                .build()
                                )
                                .setDisplayName("Free shipping")
                                .setDeliveryEstimate(
                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                                                .setMinimum(
                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                                                                .setUnit(
                                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                                                                )
                                                                .setValue(5L)
                                                                .build()
                                                )
                                                .setMaximum(
                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                                                                .setUnit(
                                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                                                                )
                                                                .setValue(7L)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
        result.add(option1);
        var option2 = SessionCreateParams.ShippingOption.builder()
                .setShippingRateData(
                        SessionCreateParams.ShippingOption.ShippingRateData.builder()
                                .setType(
                                        SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
                                )
                                .setFixedAmount(
                                        SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                                                .setAmount(1500L)
                                                .setCurrency("usd")
                                                .build()
                                )
                                .setDisplayName("Next day air")
                                .setDeliveryEstimate(
                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                                                .setMinimum(
                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                                                                .setUnit(
                                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                                                                )
                                                                .setValue(1L)
                                                                .build()
                                                )
                                                .setMaximum(
                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                                                                .setUnit(
                                                                        SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                                                                )
                                                                .setValue(1L)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
        result.add(option2);
        return result;
    }
}
