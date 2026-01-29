package com.company.atms.transaction.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.company.atms.account.entity.Account;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions", indexes = { @Index(name = "idx_tx_account", columnList = "account_id"),
		@Index(name = "idx_tx_reference", columnList = "transaction_reference", unique = true),
		@Index(name = "idx_tx_created_at", columnList = "created_at") })
@Getter
@NoArgsConstructor
public class Transaction {
	/*
	 * Represents money movement. Entity: TransactionEntity Fields: id (UUID)
	 * transactionRef (unique, human-readable) account (ManyToOne) type (DEBIT /
	 * CREDIT) amount status (SUCCESS, FAILED, PENDING) createdAt description This
	 * is the heart of the project.
	 */
	@Id
	@UuidGenerator
	@Setter(value = AccessLevel.NONE)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "transaction_reference", nullable = false, unique = true, length = 50)
	private String transactionReference = UUID.randomUUID().toString();

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tx_account"))
	@Setter(AccessLevel.NONE)
	private Account account;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false, length = 20)
	private TransactionType transactionType;

	@NotNull
	@Positive
	@Column(name = "amount", nullable = false, precision = 19, scale = 4)
	private BigDecimal amount;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private TransactionStatus transactionStatus;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "description", length = 255)
	private String description;

	public void setAccount(Account account) {
		this.account = account;
	}

	public Transaction(String transactionReference, Account account, TransactionType transactionType, BigDecimal amount,
			String description) {
		if (amount == null || amount.signum() <= 0) {
			throw new IllegalArgumentException("Transaction amount must be positive");
		}

		this.transactionReference = transactionReference;
		this.account = account;
		this.transactionType = transactionType;
		this.amount = amount;
		this.description = description;
		this.transactionStatus = TransactionStatus.SUCCESS;
		if (transactionType == TransactionType.DEBIT) {
		    account.debit(amount);
		} else {
		    account.credit(amount);
		}
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction other = (Transaction) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", reference='" + transactionReference + '\'' +
                ", type=" + transactionType +
                ", amount=" + amount +
                ", status=" + transactionStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
