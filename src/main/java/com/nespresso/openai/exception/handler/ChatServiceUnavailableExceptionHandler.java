package com.nespresso.openai.exception.handler;

import com.nespresso.common.exception.dto.ApiErrorResponse;
import com.nespresso.common.exception.handler.ApiErrorResponseCreator;
import com.nespresso.common.exception.handler.ErrorDebugMessageCreator;
import com.nespresso.openai.exception.ChatServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ChatServiceUnavailableExceptionHandler {

    private final ApiErrorResponseCreator apiErrorResponseCreator;
    private final ErrorDebugMessageCreator errorDebugMessageCreator;

    @ExceptionHandler(ChatServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleChatServiceUnavailableException(final ChatServiceUnavailableException exception) {
        ApiErrorResponse apiErrorResponse = apiErrorResponseCreator.buildResponse(exception, HttpStatus.BAD_REQUEST);

        log.warn("Handle chat service unavailable exception: failed: message: {}, debugMessage: {}.",
                apiErrorResponse.message(), errorDebugMessageCreator.buildErrorDebugMessage(exception));
        return apiErrorResponse;
    }
}

