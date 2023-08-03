package com.chris.money.requests;

public class AccountCreateRequest {
	private String accountNumber;

	private Double balance;

	public AccountCreateRequest() {
		// Default constructor for deserialization
	}

	public AccountCreateRequest(String accountNumber, Double balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
}
