package com.nespresso.security.exception;

public class AccountLockedException extends RuntimeException {

	public AccountLockedException() {
		super("Your account is locked.");
	}
}
