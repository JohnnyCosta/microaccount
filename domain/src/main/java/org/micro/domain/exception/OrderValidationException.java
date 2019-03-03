package org.micro.domain.exception;

public class OrderValidationException extends RuntimeException {
	public OrderValidationException(final String message) {
		super(message);
	}
}
