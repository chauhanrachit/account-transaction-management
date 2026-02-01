package com.company.atms.transaction.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.company.atms.transaction.entity.TransactionStatus;
import com.company.atms.transaction.entity.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponse {

	private UUID transactionId;
	private String reference;
	private UUID accountId;
	private TransactionType type;
	private BigDecimal amount;
	private TransactionStatus status;
	private Instant createdAt;
}
