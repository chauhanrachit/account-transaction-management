package com.company.atms.transaction.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.company.atms.transaction.entity.Transaction;
import com.company.atms.transaction.entity.TransactionStatus;
import com.company.atms.transaction.entity.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponse {

	private String transactionReference;
	private TransactionStatus status;
	private BigDecimal amount;
	private Instant createdAt;

    public static TransactionResponse from(Transaction tx) {
        return TransactionResponse.builder()
                .transactionReference(tx.getTransactionReference())
                .status(tx.getTransactionStatus())
                .amount(tx.getAmount())
                .createdAt(tx.getCreatedAt())
                .build();
    }
    
//	private UUID transactionId;
}
