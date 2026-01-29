package com.company.atms.account.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.company.atms.customer.entity.Customer;
import com.company.atms.transaction.entity.Transaction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts", indexes = { @Index(name = "idx_account_number", columnList = "account_number", unique = true),
		@Index(name = "idx_account_customer", columnList = "customer_id") })
@Getter
@NoArgsConstructor
public class Account {

	/*
	 * Represents a bank account. Entity: AccountEntity Important design decision:
	 * Balance is NOT derived from transactions every time Balance is stored and
	 * updated transactionally Fields: id (UUID) accountNumber (unique) accountType
	 * (SAVINGS, CURRENT) balance currency status (ACTIVE, FROZEN, CLOSED)/////
	 * customer (ManyToOne) createdAt
	 */
	@Id
	@UuidGenerator
	@Setter(value = AccessLevel.NONE)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull
	@Column(name = "account_number", nullable = false, unique = true, length = 30)
	private String accountNumber;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false, length = 30)
	private AccountType accountType;

	@NotNull
	@PositiveOrZero
	@Setter(AccessLevel.NONE)
	@Column(name = "balance", nullable = false, precision = 19, scale = 4)
	private BigDecimal balance;

	@Enumerated(EnumType.STRING)
	@Column(name = "currency", length = 3)
	private CurrencyCode currency;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private AccountStatus accountStatus;

	@Setter(AccessLevel.NONE)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_customer"))
	private Customer customer;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Transaction> transactions = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public Account(String accountNumber, AccountType accountType, CurrencyCode currency, Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("Customer is required");
		}
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.currency = currency;
		this.balance = BigDecimal.ZERO;
		this.accountStatus = AccountStatus.ACTIVE;
		this.customer = customer;
	}

	public void credit(BigDecimal amount) {
		if (amount == null || amount.signum() <= 0) {
			throw new IllegalArgumentException("Credit amount must be positive");
		}
		this.balance = this.balance.add(amount);
	}

	public void debit(BigDecimal amount) {
		if (amount == null || amount.signum() <= 0) {
			throw new IllegalArgumentException("Debit amount must be positive");
		}
		if (this.accountStatus != AccountStatus.ACTIVE) {
			throw new IllegalStateException("Account is not active");
		}
		if (this.balance.compareTo(amount) < 0) {
			throw new IllegalStateException("Insufficient balance");
		}
		this.balance = this.balance.subtract(amount);
	}

	public List<Transaction> getTransactions() {
		return List.copyOf(transactions);
	}

	public void addTransaction(Transaction tx) {
		transactions.add(tx);
		tx.setAccount(this);
	}

	public void removeTransaction(Transaction tx) {
		transactions.remove(tx);
		tx.setAccount(null);
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Account))
			return false;
		Account other = (Account) o;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "Account{" + "id=" + id + ", accountNumber='" + accountNumber + '\'' + ", accountType=" + accountType
				+ ", balance=" + balance + ", currency='" + currency + '\'' + ", status=" + accountStatus + '}';
	}
//	public void setCustomer(Customer customer2) {
//		// TODO Auto-generated method stub
//
//	}
}
