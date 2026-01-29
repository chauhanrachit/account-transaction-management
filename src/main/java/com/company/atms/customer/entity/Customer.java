package com.company.atms.customer.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.company.atms.account.entity.Account;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers", indexes = { @Index(name = "idx_customer_email", columnList = "email", unique = true),
		@Index(name = "idx_customer_phone", columnList = "phone_number", unique = true) })

@Getter
@NoArgsConstructor
public class Customer {
	/*
	 * Represents the account holder. Key ideas: One customer → many accounts
	 * KYC-like fields (but simple) Entity: CustomerEntity Fields (conceptual): id
	 * (UUID) firstName lastName email phone status (ACTIVE / BLOCKED) createdAt
	 */

	@Id
	@UuidGenerator
	@Setter(value = AccessLevel.NONE)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;

	@NotBlank
	@Column(name = "email", nullable = false, length = 150, unique = true)
	private String email;

	@NotBlank
	@Column(name = "phone_number", nullable = false, length = 20, unique = true)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private CustomerStatus customerStatus;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Account> accounts = new ArrayList<>();;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	// Domain methods
	public List<Account> getAccounts() {
		return List.copyOf(accounts);
	}
	
	public Customer(String firstName, String lastName, String email, String phoneNumber) {
	    this.firstName = firstName;
	    this.lastName = lastName;
	    this.email = email;
	    this.phoneNumber = phoneNumber;
	    this.customerStatus = CustomerStatus.ACTIVE;
	}
		
	public void addAccount(Account account) {
		if (account == null) {
			throw new IllegalArgumentException("Account cannot be null");
		}
		if (!accounts.contains(account)) {
		    accounts.add(account);
		    account.setCustomer(this);
		}
	}

	public void removeAccount(Account account) {
		if (account == null)
			return;
		accounts.remove(account);
		account.setCustomer(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Customer))
			return false;
		Customer other = (Customer) o;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
				+ ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", status=" + customerStatus
				+ '}';
	}
}
