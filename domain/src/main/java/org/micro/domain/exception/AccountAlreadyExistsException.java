package org.micro.domain.exception;

public class AccountAlreadyExistsException extends RuntimeException {
	public AccountAlreadyExistsException(final String message) {
		super(message);
	}
}
