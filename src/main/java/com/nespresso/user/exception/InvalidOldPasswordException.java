package com.nespresso.user.exception;

import lombok.Getter;

@Getter
public class InvalidOldPasswordException extends RuntimeException {

    private final String userName;

    public InvalidOldPasswordException(String userEmail) {
        super(String.format("User with userEmail = '%s' provided incorrect password.", userEmail));
        this.userName = null;
    }
}
