package com.chris.money.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.chris.money.domain.Account;
import com.chris.money.requests.AccountCreateRequest;
import com.chris.money.services.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	private final AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * Endpoint for retrieving an account by its ID.
	 * 
	 * @param id The ID of the account to be retrieved.
	 * @return ResponseEntity with the account information if found (HTTP status
	 *         code 200 - OK), or ResponseEntity with HTTP status code 404 (Not
	 *         Found) if the account is not found.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable Long id) {
		Account account = accountService.getAccountById(id);
		if (account != null) {
			return ResponseEntity.ok(account);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found !");
		}
	}

	/**
	 * Endpoint for creating a new account.
	 * 
	 * @param request The AccountCreateRequest containing the details of the new
	 *                account.
	 * @return ResponseEntity with the newly created account information (HTTP
	 *         status code 201 - Created).
	 */
	@PostMapping
	public ResponseEntity<?> createAccount( @RequestBody AccountCreateRequest request) {
		// Validate accountNumber and balance
		if (request.getAccountNumber() == null) {
			return ResponseEntity.badRequest().body("Account number must not be blank !");
		}
		
		if (request.getBalance()  == null) {
			return ResponseEntity.badRequest().body("Balance not set !");
		}

		if (request.getBalance() <= 0) {
			return ResponseEntity.badRequest().body("Balance must be greater than 0 !");
		}
		
		Account account = accountService.createAccount(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(account.getId())
				.toUri();
		return ResponseEntity.created(location).body(account);
	}

}
