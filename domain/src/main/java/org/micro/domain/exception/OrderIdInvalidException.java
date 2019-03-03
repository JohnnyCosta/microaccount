package org.micro.domain.exception;

public class OrderIdInvalidException extends RuntimeException {
	public OrderIdInvalidException(final String message) {
		super(message);
	}
}
