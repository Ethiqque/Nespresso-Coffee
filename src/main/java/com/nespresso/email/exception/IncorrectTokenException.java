package com.nespresso.email.exception;

public class IncorrectTokenException extends RuntimeException {

    public IncorrectTokenException() {
        super("Incorrect token");
    }
}
