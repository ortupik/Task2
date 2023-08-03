package com.chris.money;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.chris.money.domain.Account;
import com.chris.money.exceptions.InsufficientBalanceException;
import com.chris.money.exceptions.InvalidTransferException;
import com.chris.money.repositories.AccountRepository;
import com.chris.money.repositories.TransferRepository;
import com.chris.money.requests.AccountCreateRequest;
import com.chris.money.services.AccountService;
import com.chris.money.services.TransferService;

import javassist.NotFoundException;

@SpringBootTest
public class TransferServiceIntegrationTest {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();
        transferRepository.deleteAll();
    }

    @Test
    public void testTransferMoney() throws InsufficientBalanceException, InvalidTransferException, NotFoundException {
        // Arrange
        AccountCreateRequest sourceAccountRequest = new AccountCreateRequest("123456789", 1000.0);
        Account sourceAccount = accountService.createAccount(sourceAccountRequest);
        AccountCreateRequest destinationAccountRequest = new AccountCreateRequest("987654321", 2000.0);
        Account destinationAccount = accountService.createAccount(destinationAccountRequest);

        // Act
        transferService.transferMoney(sourceAccount.getId(), destinationAccount.getId(), 500.0);

        // Assert
        assertEquals(500.0, sourceAccount.getBalance());
        assertEquals(2500.0, destinationAccount.getBalance());
    }

    @Test
    public void testTransferMoneyInsufficientBalance() {
        // Arrange
        AccountCreateRequest sourceAccountRequest = new AccountCreateRequest("123456789", 100.0);
        Account sourceAccount = accountService.createAccount(sourceAccountRequest);
        AccountCreateRequest destinationAccountRequest = new AccountCreateRequest("987654321", 2000.0);
        Account destinationAccount = accountService.createAccount(destinationAccountRequest);

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> transferService.transferMoney(sourceAccount.getId(), destinationAccount.getId(), 500.0));
    }

    @Test
    public void testTransferMoneyInvalidAmount() {
        // Arrange
        AccountCreateRequest sourceAccountRequest = new AccountCreateRequest("123456789", 1000.0);
        Account sourceAccount = accountService.createAccount(sourceAccountRequest);
        AccountCreateRequest destinationAccountRequest = new AccountCreateRequest("987654321", 2000.0);
        Account destinationAccount = accountService.createAccount(destinationAccountRequest);

        // Act & Assert
        assertThrows(InvalidTransferException.class, () -> transferService.transferMoney(sourceAccount.getId(), destinationAccount.getId(), -500.0));
    }
}

