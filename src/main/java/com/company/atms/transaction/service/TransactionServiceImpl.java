package com.company.atms.transaction.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.atms.account.entity.Account;
import com.company.atms.account.entity.AccountStatus;
import com.company.atms.account.repository.AccountRepository;
import com.company.atms.audit.entity.AuditAction;
import com.company.atms.audit.service.AuditService;
import com.company.atms.common.exception.DuplicateTransactionException;
import com.company.atms.common.exception.InsufficientBalanceException;
import com.company.atms.common.exception.InvalidAccountStateException;
import com.company.atms.transaction.entity.Transaction;
import com.company.atms.transaction.entity.TransactionType;
import com.company.atms.transaction.repository.TransactionRepository;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final AuditService auditService;

	public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, AuditService auditService) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.auditService=auditService;
	}

	@Override
	public Transaction credit(UUID accountId, String reference, BigDecimal amount, String description) {
		if (transactionRepository.existsByTransactionReference(reference)) {
		    throw new DuplicateTransactionException("Duplicate transaction reference: " + reference);
		}
		Account account = getAccountForUpdate(accountId);
		
		if (account.getAccountStatus() != AccountStatus.ACTIVE) {
		    throw new InvalidAccountStateException("Account not active");
		}

		Transaction tx = Transaction.createPending(
				reference, 
				account, 
				TransactionType.CREDIT, 
				amount, 
				description
		);
		transactionRepository.save(tx);

		try {
			account.credit(amount);
			tx.markSuccess();
			auditService.recordEvent(
			        AuditAction.TRANSACTION_CREDIT_SUCCESS,
			        "Transaction",
			        tx.getId(),
			        "Credit successful for amount " + amount
			    );
			return tx;
		} catch (Exception ex) {
			tx.markFailed(ex.getMessage());
			auditService.recordEvent(
			        AuditAction.TRANSACTION_CREDIT_FAILED,
			        "Transaction",
			        tx.getId(),
			        ex.getMessage()
			    );
			throw ex;
		}
	}

	@Override
	@Transactional
	public Transaction debit(UUID accountId, String reference, BigDecimal amount, String description) {
		if (transactionRepository.existsByTransactionReference(reference)) {
			throw new DuplicateTransactionException("Duplicate transaction reference: " + reference);
		}
		Account account = getAccountForUpdate(accountId);
		
		if (account.getAccountStatus() != AccountStatus.ACTIVE) {
		    throw new InvalidAccountStateException("Account not active");
		}
		
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

		Transaction tx = Transaction.createPending(
				reference, 
				account, 
				TransactionType.DEBIT, 
				amount, 
				description
		);
		transactionRepository.save(tx);

		try {
			account.debit(amount);
			tx.markSuccess();
			auditService.recordEvent(
				        AuditAction.TRANSACTION_DEBIT_SUCCESS,
				        "Transaction",
				        tx.getId(),
				        "Debit successful for amount " + amount
				    );
			return tx;
		} catch (Exception ex) {
			tx.markFailed(ex.getMessage());
			auditService.recordEvent(
			        AuditAction.TRANSACTION_DEBIT_FAILED,
			        "Transaction",
			        tx.getId(),
			        ex.getMessage()
			    );
			
			throw ex;
		}
	}

	private Account getAccountForUpdate(UUID accountId) {
		return accountRepository.findByIdForUpdate(accountId)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));
	}
}
