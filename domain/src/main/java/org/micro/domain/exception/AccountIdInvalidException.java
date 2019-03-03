package org.micro.domain.exception;

public class AccountIdInvalidException extends RuntimeException {
	public AccountIdInvalidException(final String message) {
		super(message);
	}
}
