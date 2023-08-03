package com.chris.money.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chris.money.domain.Account;
import com.chris.money.domain.Transfer;
import com.chris.money.exceptions.InsufficientBalanceException;
import com.chris.money.exceptions.InvalidTransferException;
import com.chris.money.repositories.AccountRepository;
import com.chris.money.repositories.TransferRepository;

import javassist.NotFoundException;

@Service
public class TransferService {

	private final AccountRepository accountRepository;
	private final TransferRepository transferRepository;

	@Autowired
	public TransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
		this.accountRepository = accountRepository;
		this.transferRepository = transferRepository;
	}

	/**
	 * Performs a money transfer from the source account to the destination account.
	 *
	 * @param sourceAccountId      The ID of the source account.
	 * @param destinationAccountId The ID of the destination account.
	 * @param amount               The amount to be transferred.
	 * @return 
	 * @throws NotFoundException            If either the source or destination
	 *                                      account is not found.
	 * @throws InsufficientBalanceException If the source account does not have
	 *                                      enough funds for the transfer.
	 * @throws InvalidTransferException     If the transfer amount is less than or
	 *                                      equal to zero.
	 */
	@Transactional
	public Transfer transferMoney(Long sourceAccountId, Long destinationAccountId, double amount)
			throws NotFoundException, InsufficientBalanceException, InvalidTransferException {
		// Validate transfer amount
		if (amount <= 0) {
			throw new InvalidTransferException("Transfer amount must be greater than zero.");
		}

		// Retrieve the source and destination accounts from the database
		Optional<Account> sourceAccountOptional = accountRepository.findById(sourceAccountId);
		Account sourceAccount = sourceAccountOptional
				.orElseThrow(() -> new NotFoundException("Source account not found with ID: " + sourceAccountId));

		Optional<Account> destinationAccountOptional = accountRepository.findById(destinationAccountId);
		Account destinationAccount = destinationAccountOptional.orElseThrow(
				() -> new NotFoundException("Destination account not found with ID: " + destinationAccountId));

		// Perform the money transfer
		if (sourceAccount.getBalance() >= amount) {
			sourceAccount.setBalance(sourceAccount.getBalance() - amount);
			destinationAccount.setBalance(destinationAccount.getBalance() + amount);

			// Create a transfer record
			Transfer transfer = new Transfer(sourceAccount, destinationAccount, amount);
			return transferRepository.save(transfer);
		} else {
			throw new InsufficientBalanceException("Insufficient balance in the source account.");
		}
	}
}
