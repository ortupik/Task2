package com.chris.money;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.xml.bind.ValidationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.chris.money.domain.Account;
import com.chris.money.repositories.AccountRepository;
import com.chris.money.requests.AccountCreateRequest;
import com.chris.money.services.AccountService;

@SpringBootTest
public class AccountServiceIntegrationTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	@DirtiesContext
	public void testCreateAccount_ValidRequest_CreatesAccount() {
		// Arrange
		AccountCreateRequest request = new AccountCreateRequest("12345", 1500.0);

		// Act
		Account result = accountService.createAccount(request);

		// Assert
		assertNotNull(result.getId());
		assertEquals(request.getAccountNumber(), result.getAccountNumber());
		assertEquals(request.getBalance(), result.getBalance());
	}

	@Test
	@DirtiesContext
	public void testCreateAccount_InvalidRequest_ThrowsException() {
		// Arrange
		AccountCreateRequest request = new AccountCreateRequest("", -100.0);

		// Act & Assert
		assertThrows(ValidationException.class, () -> accountService.createAccount(request));
	}

	@Test
	public void testGetAccountById_ExistingId_ReturnsAccount() {
		// Arrange
		Account account = new Account("John Doe", 2000.0);
		accountRepository.save(account);

		// Act
		Account result = accountService.getAccountById(account.getId());

		// Assert
		assertNotNull(result);
		assertEquals(account.getId(), result.getId());
		assertEquals(account.getAccountNumber(), result.getAccountNumber());
		assertEquals(account.getBalance(), result.getBalance());
	}

	@Test
	public void testGetAccountById_NonExistingId_ReturnsNull() {
		// Act
		Account result = accountService.getAccountById(1000L);

		// Assert
		assertNull(result);
	}
}

