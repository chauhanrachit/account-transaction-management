package com.company.atms.account.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.atms.account.entity.Account;
import com.company.atms.account.entity.AccountType;
import com.company.atms.account.entity.CurrencyCode;
import com.company.atms.account.repository.AccountRepository;
import com.company.atms.customer.entity.Customer;
import com.company.atms.customer.service.CustomerService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final CustomerService customerService;

	public AccountServiceImpl(AccountRepository accountRepository, CustomerService customerService) {
		this.accountRepository = accountRepository;
		this.customerService = customerService;
	}

	@Override
	public Account openAccount(UUID customerId, AccountType accountType, CurrencyCode currency) {

		Customer customer = customerService.getCustomerById(customerId);

		Account account = new Account(generateAccountNumber(), accountType, currency, customer);

		return accountRepository.save(account);
	}

	@Override
	public Account getAccountByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));
	}

	private String generateAccountNumber() {
		return "ACCT-" + System.currentTimeMillis();
	}
}
