package com.company.atms.customer.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.atms.customer.entity.Customer;
import com.company.atms.customer.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Customer createCustomer(String firstName, String lastName, String email, String phoneNumber) {
		if (customerRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("Email already exists");
		}

		if (customerRepository.existsByPhoneNumber(phoneNumber)) {
			throw new IllegalArgumentException("Phone number already exists");
		}

		Customer customer = new Customer(firstName, lastName, email, phoneNumber);

		return customerRepository.save(customer);
	}

	@Override
	public Customer getCustomerById(UUID customerId) {
		return customerRepository.findById(customerId)
				.orElseThrow(() -> new IllegalArgumentException("Customer not found"));
	}

}
