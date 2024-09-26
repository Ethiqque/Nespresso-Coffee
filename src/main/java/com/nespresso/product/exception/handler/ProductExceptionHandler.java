package com.nespresso.product.exception.handler;

import com.nespresso.common.exception.dto.ApiErrorResponse;
import com.nespresso.common.exception.handler.ApiErrorResponseCreator;
import com.nespresso.common.exception.handler.ErrorDebugMessageCreator;
import com.nespresso.product.exception.GetProductsBadRequestException;
import com.nespresso.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ProductExceptionHandler {

    private final ApiErrorResponseCreator apiErrorResponseCreator;
    private final ErrorDebugMessageCreator errorDebugMessageCreator;

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleProductNotFoundException(final ProductNotFoundException exception) {
        ApiErrorResponse apiErrorResponse = apiErrorResponseCreator.buildResponse(exception, HttpStatus.NOT_FOUND);

        log.error("Handle product not found exception: failed: message: {}, debugMessage: {}.",
                apiErrorResponse.message(), errorDebugMessageCreator.buildErrorDebugMessage(exception));

        return apiErrorResponse;
    }

    @ExceptionHandler(GetProductsBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleGetProductsBadRequestException(final GetProductsBadRequestException exception) {
        ApiErrorResponse apiErrorResponse = apiErrorResponseCreator.buildResponse(exception, HttpStatus.BAD_REQUEST);

        log.warn("Handle product invalid property exception: failed: message: {}, debugMessage: {}.",
                apiErrorResponse.message(), errorDebugMessageCreator.buildErrorDebugMessage(exception));

        return apiErrorResponse;
    }
}
