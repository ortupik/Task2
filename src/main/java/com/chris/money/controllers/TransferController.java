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
import com.chris.money.domain.Transfer;
import com.chris.money.exceptions.InsufficientBalanceException;
import com.chris.money.exceptions.InvalidTransferException;
import com.chris.money.requests.AccountCreateRequest;
import com.chris.money.requests.TransferRequest;
import com.chris.money.services.TransferService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/transfers")
public class TransferController {

	private final TransferService transferService;

	@Autowired
	public TransferController(TransferService transferService) {
		this.transferService = transferService;
	}

	
	/**
	 * Endpoint for transfers
	 * 
	 * @param request The TransferRequest containing the details of the transfers
	 * @return ResponseEntity with the transfer transaction details (HTTP
	 *         status code 201 - Created).
	 * @throws NotFoundException 
	 * @throws InvalidTransferException 
	 * @throws InsufficientBalanceException 
	 */
	@PostMapping
	public ResponseEntity<?> createAccount( @RequestBody TransferRequest request) throws InsufficientBalanceException, InvalidTransferException, NotFoundException {
		// Validate accountNumber and balance
		if (request.getSourceAccountId() == null) {
			return ResponseEntity.badRequest().body("Source Destination ID not set!");
		}
		
		if (request.getDestinationAccountId()  == null) {
			return ResponseEntity.badRequest().body("Destination Account ID not set !");
		}
		Double amount = request.getAmount();
		if (amount.isNaN()) {
			return ResponseEntity.badRequest().body("Amount not set !");
		}

		if (request.getAmount() <= 0) {
			return ResponseEntity.badRequest().body("Amount must be greater than 0 !");
		}
		
		Transfer transfer = transferService.transferMoney(request.getSourceAccountId(), request.getDestinationAccountId(), request.getAmount());
		
		return ResponseEntity.ok(transfer);
	}

}
