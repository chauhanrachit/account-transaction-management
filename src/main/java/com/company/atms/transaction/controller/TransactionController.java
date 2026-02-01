package com.company.atms.transaction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.atms.transaction.dto.CreditRequest;
import com.company.atms.transaction.dto.DebitRequest;
import com.company.atms.transaction.dto.TransactionResponse;
import com.company.atms.transaction.entity.Transaction;
import com.company.atms.transaction.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {
	
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/debit")
	public ResponseEntity<TransactionResponse> debit(@Valid @RequestBody DebitRequest request) {

		Transaction tx = transactionService.debit(
				request.getAccountId(), 
				request.getReference(),
				request.getAmount(), 
				request.getDescription()
		);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(toResponse(tx));
	}

	@PostMapping("/credit")
	public ResponseEntity<TransactionResponse> credit(@Valid @RequestBody CreditRequest request) {

		Transaction tx = transactionService.credit(
				request.getAccountId(),
				request.getReference(),
				request.getAmount(), 
				request.getDescription());

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(toResponse(tx));
	}
	
	private TransactionResponse toResponse(Transaction tx) {
        return TransactionResponse.builder()
                .transactionId(tx.getId())
                .reference(tx.getTransactionReference())
                .accountId(tx.getAccount().getId())
                .type(tx.getTransactionType())
                .amount(tx.getAmount())
                .status(tx.getTransactionStatus())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
