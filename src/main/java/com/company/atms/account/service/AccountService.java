package com.company.atms.account.service;

import java.util.UUID;

import com.company.atms.account.entity.Account;
import com.company.atms.account.entity.AccountType;
import com.company.atms.account.entity.CurrencyCode;

public interface AccountService {
	Account openAccount(
            UUID customerId,
            AccountType accountType,
            CurrencyCode currency
    );

    Account getAccountByAccountNumber(String accountNumber);
}
