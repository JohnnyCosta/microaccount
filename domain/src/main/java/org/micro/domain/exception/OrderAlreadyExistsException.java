package org.micro.domain.exception;

public class OrderAlreadyExistsException extends RuntimeException {
	public OrderAlreadyExistsException(final String message) {
		super(message);
	}
}
