package com.chris.money.requests;

public class TransferRequest {
	private Long sourceAccountId;
	private Long destinationAccountId;
	private double amount;

	public TransferRequest() {
		// Default constructor for deserialization
	}

	public TransferRequest(Long sourceAccountId, Long destinationAccountId, double amount) {
		this.sourceAccountId = sourceAccountId;
		this.destinationAccountId = destinationAccountId;
		this.amount = amount;
	}

	public Long getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(Long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public Long getDestinationAccountId() {
		return destinationAccountId;
	}

	public void setDestinationAccountId(Long destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
