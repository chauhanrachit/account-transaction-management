package com.company.atms.transaction.controller;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.atms.common.response.ApiResponse;
import com.company.atms.transaction.dto.CreditRequest;
import com.company.atms.transaction.dto.DebitRequest;
import com.company.atms.transaction.dto.TransactionResponse;
import com.company.atms.transaction.entity.Transaction;
import com.company.atms.transaction.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;
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
	public ResponseEntity<ApiResponse<TransactionResponse>> debit(
            @Valid @RequestBody DebitRequest request,
            HttpServletRequest httpRequest) {

        Transaction tx = transactionService.debit(
                request.getAccountId(),
                request.getReference(),
                request.getAmount(),
                request.getDescription()
        );

        return ResponseEntity.ok(
                ApiResponse.<TransactionResponse>builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.OK.value())
                        .message("Debit successful")
                        .data(TransactionResponse.from(tx))
                        .path(httpRequest.getRequestURI())
                        .build()
        );
    }

	@PostMapping("/credit")
    public ResponseEntity<ApiResponse<TransactionResponse>> credit(
            @Valid @RequestBody CreditRequest request, HttpServletRequest httpRequest) {

        Transaction tx = transactionService.credit(
                request.getAccountId(),
                request.getReference(),
                request.getAmount(),
                request.getDescription()
        );

        return ResponseEntity.ok(
                ApiResponse.<TransactionResponse>builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.OK.value())
                        .message("Credit successful")
                        .data(TransactionResponse.from(tx))
                        .path(httpRequest.getRequestURI())
                        .build()
        );
    }
}
