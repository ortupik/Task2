package com.chris.money;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.chris.money.domain.Account;
import com.chris.money.repositories.AccountRepository;
import com.chris.money.requests.AccountCreateRequest;
import com.chris.money.services.AccountService;

@SpringBootTest
public class AccountServiceUnitTest {

    @MockBean
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(accountRepository);
    }

    @Test
    public void testCreateAccount() {
        AccountCreateRequest request = new AccountCreateRequest("AC123", 100.0);
        Account account = new Account("AC123", 100.0);

        when(accountRepository.save(account)).thenReturn(account);

        Account createdAccount = accountService.createAccount(request);
        assertEquals(account.getAccountNumber(), createdAccount.getAccountNumber());
        assertEquals(account.getBalance(), createdAccount.getBalance());
    }

    @Test
    public void testGetAccountById() {
        Long accountId = 1L;
        Account account = new Account("AC123", 100.0);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        Account retrievedAccount = accountService.getAccountById(accountId);
        assertEquals(accountId, retrievedAccount.getId());
    }
}
