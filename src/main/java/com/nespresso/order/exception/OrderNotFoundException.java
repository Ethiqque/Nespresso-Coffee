package com.nespresso.order.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(final UUID orderId) {
        super(String.format("Order with id = '%s' was not found. ", orderId));
    }
}
