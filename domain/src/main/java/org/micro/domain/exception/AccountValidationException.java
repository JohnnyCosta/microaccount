package org.micro.domain.exception;

public class AccountValidationException extends RuntimeException {
	public AccountValidationException(final String message) {
		super(message);
	}
}
