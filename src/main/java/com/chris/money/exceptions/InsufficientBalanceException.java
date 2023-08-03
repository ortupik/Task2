package com.chris.money.exceptions;

public class InsufficientBalanceException extends RuntimeException {

	public InsufficientBalanceException(String message) {
		super(message);
	}
}
