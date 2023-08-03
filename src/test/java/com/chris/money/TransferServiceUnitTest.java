package com.chris.money;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;

import com.chris.money.domain.Account;
import com.chris.money.exceptions.InsufficientBalanceException;
import com.chris.money.exceptions.InvalidTransferException;
import com.chris.money.repositories.AccountRepository;
import com.chris.money.repositories.TransferRepository;
import com.chris.money.requests.AccountCreateRequest;
import com.chris.money.services.AccountService;
import com.chris.money.services.TransferService;

import javassist.NotFoundException;

@SpringBootTest(classes=Application.class)
public class TransferServiceUnitTest {

	@Mock
	private AccountRepository accountRepository;
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	private AccountService accountService;

	@Test
	public void testTransferMoney_ValidTransfer_Successful() throws InsufficientBalanceException, InvalidTransferException, NotFoundException {
		
		// Arrange
		String acc1 = UUID.randomUUID().toString();
		String acc2 = UUID.randomUUID().toString();
		
		Account sourceAccount = accountService.createAccount(new AccountCreateRequest(acc1, 2000.0));
		Account destinationAccount = accountService.createAccount(new AccountCreateRequest(acc2, 1000.0));
		
		// Act
		transferService.transferMoney(sourceAccount.getId(), destinationAccount.getId(), 500.0).getSourceAccount().getBalance();
	
		// Assert
	    assertEquals(1500.0, accountService.getAccountById(sourceAccount.getId()).getBalance());
		assertEquals(1500.0, accountService.getAccountById(destinationAccount.getId()).getBalance());
	}

	@Test
	public void testTransferMoney_InsufficientBalance_ThrowsException() {
		
		// Arrange
		String acc1 = UUID.randomUUID().toString();
		String acc2 = UUID.randomUUID().toString();
		
		Account sourceAccount = accountService.createAccount(new AccountCreateRequest(acc1, 500.0));
		Account destinationAccount = accountService.createAccount(new AccountCreateRequest(acc2, 1000.0));
		
		when(accountRepository.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
		when(accountRepository.findById(destinationAccount.getId())).thenReturn(Optional.of(destinationAccount));

		// Act & Assert
		assertThrows(InsufficientBalanceException.class,
				() -> transferService.transferMoney(sourceAccount.getId(), destinationAccount.getId(), 1300.0));
	}

	@Test
	public void testTransferMoney_InvalidSourceAccount_ThrowsException() {
		// Arrange
		Account destinationAccount = new Account("2334544", 1000.0);
		when(accountRepository.findById(100L)).thenReturn(Optional.empty());
		when(accountRepository.findById(destinationAccount.getId())).thenReturn(Optional.of(destinationAccount));

		// Act & Assert
		assertThrows(NotFoundException.class,
				() -> transferService.transferMoney(100L, destinationAccount.getId(), 500.0));
	}

	@Test
	public void testTransferMoney_InvalidDestinationAccount_ThrowsException() {
		// Arrange
		Account sourceAccount = new Account("123456", 2000.0);
		sourceAccount.setId(102121L);
		
		when(accountRepository.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
		when(accountRepository.findById(100L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class,
				() -> transferService.transferMoney(sourceAccount.getId(), 100L, 500.0));
	}

	@Test
	public void testTransferMoney_NegativeTransferAmount_ThrowsException() {
		// Arrange
		Account sourceAccount = new Account("123456", 2000.0);
		Account destinationAccount = new Account("2345678", 1000.0);
		when(accountRepository.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
		when(accountRepository.findById(destinationAccount.getId())).thenReturn(Optional.of(destinationAccount));

		// Act & Assert
		assertThrows(InvalidTransferException.class,
				() -> transferService.transferMoney(sourceAccount.getId(), destinationAccount.getId(), -100.0));
	}
}
