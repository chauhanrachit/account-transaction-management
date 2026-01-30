package com.company.atms.transaction.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.company.atms.transaction.entity.Transaction;

public interface TransactionService {
	
	Transaction credit(UUID accountId, String reference, BigDecimal amount, String description);
    Transaction debit (UUID accountId, String reference, BigDecimal amount, String description);
}
