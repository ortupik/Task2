package com.chris.money.exceptions;

public class InvalidTransferException extends RuntimeException {

	public InvalidTransferException(String message) {
		super(message);
	}
}
