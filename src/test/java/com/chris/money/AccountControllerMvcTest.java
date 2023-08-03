package com.chris.money;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.chris.money.controllers.AccountController;
import com.chris.money.domain.Account;
import com.chris.money.requests.AccountCreateRequest;
import com.chris.money.services.AccountService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
public class AccountControllerMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private AccountController accountController;

	@Test
	void testGetAccountById() throws Exception {
		Long accountId = 1L;
		Account account = new Account("12345678", 1000.0);
		when(accountService.getAccountById(accountId)).thenReturn(Optional.of(account).get());

		mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}", accountId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountId));
	}

	@Test
	void testGetAccountById_NotFound() throws Exception {
		Long nonExistentAccountId = 999L;
		when(accountService.getAccountById(nonExistentAccountId)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}", nonExistentAccountId))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void testCreateAccount() throws Exception {
		AccountCreateRequest request = new AccountCreateRequest("121211", 1000.0);
		Long accountId = 1L;
		Account account = new Account( request.getAccountNumber(), request.getBalance());
		when(accountService.createAccount(request)).thenReturn(account);

		mockMvc.perform(MockMvcRequestBuilders.post("/accounts").contentType("application/json")
				.content("{ \"accountNumber\": \"121211\", \"balance\": 1000.0 }"))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountId));
	}

	@Test
	void testCreateAccount_InvalidRequest() throws Exception {
		// Test creating an account with invalid request (e.g., missing holderName)
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts").contentType("application/json")
				.content("{ \"balance\": 1000.0 }")).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

}
