package com.chris.money.services;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chris.money.domain.Account;
import com.chris.money.repositories.AccountRepository;
import com.chris.money.requests.AccountCreateRequest;

@Service
public class AccountService {

	private final AccountRepository accountRepository;

	private final Logger logger = LoggerFactory.getLogger(AccountService.class);

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Account createAccount(AccountCreateRequest request) {
		logger.info("Creating account: {}");
		Account account = new Account(request.getAccountNumber(), request.getBalance());
		return accountRepository.save(account);
	}

	public Account getAccountById(Long accountId) {
		logger.info("Getting account by ID: {}");
		return accountRepository.findById(accountId).orElse(null);
	}

}
