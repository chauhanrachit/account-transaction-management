package com.company.atms.transaction.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.atms.account.entity.Account;
import com.company.atms.account.repository.AccountRepository;
import com.company.atms.transaction.entity.Transaction;
import com.company.atms.transaction.entity.TransactionType;
import com.company.atms.transaction.repository.TransactionRepository;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;

	public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	public Transaction credit(UUID accountId, String reference, BigDecimal amount, String description) {
		if (transactionRepository.existsByTransactionReference(reference)) {
		    throw new IllegalStateException("Duplicate transaction");
		}
		Account account = getAccountForUpdate(accountId);

		Transaction tx = Transaction.createPending(reference, account, TransactionType.CREDIT, amount, description);
		transactionRepository.save(tx);

		try {
			account.credit(amount);
			tx.markSuccess();
			return tx;
		} catch (Exception ex) {
			tx.markFailed(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public Transaction debit(UUID accountId, String reference, BigDecimal amount, String description) {
		if (transactionRepository.existsByTransactionReference(reference)) {
			throw new IllegalStateException("Duplicate transaction");
		}
		Account account = getAccountForUpdate(accountId);

		Transaction tx = Transaction.createPending(reference, account, TransactionType.DEBIT, amount, description);
		transactionRepository.save(tx);

		try {
			account.debit(amount);
			tx.markSuccess();
			return tx;
		} catch (Exception ex) {
			tx.markFailed(ex.getMessage());
			throw ex;
		}
	}

	private Account getAccountForUpdate(UUID accountId) {
		return accountRepository.findByIdForUpdate(accountId)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));
	}
}
