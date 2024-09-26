package com.nespresso.order.exception.handler;

import com.nespresso.common.exception.dto.ApiErrorResponse;
import com.nespresso.common.exception.handler.ApiErrorResponseCreator;
import com.nespresso.common.exception.handler.ErrorDebugMessageCreator;
import com.nespresso.openapi.dto.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice(basePackages = {"com.nespresso.order.endpoint"})
@RequiredArgsConstructor
public class OrderExceptionHandler {

    private final ApiErrorResponseCreator apiErrorResponseCreator;
    private final ErrorDebugMessageCreator errorDebugMessageCreator;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMethodArgumentNotValidException(final MethodArgumentTypeMismatchException exception) {
        String message = "Incorrect status value. Supported status: " + Arrays.toString(OrderStatus.values());

        ApiErrorResponse apiErrorResponse = apiErrorResponseCreator.buildResponse(message, HttpStatus.BAD_REQUEST);
        log.error("Handle method argument type mismatch exception: failed: message: {}, debugMessage: {}.",
                message, errorDebugMessageCreator.buildErrorDebugMessage(exception));

        return apiErrorResponse;
    }
}
