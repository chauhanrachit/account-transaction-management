package com.company.atms.customer.service;

import java.util.UUID;

import com.company.atms.customer.entity.Customer;

public interface CustomerService {
	
	Customer createCustomer(
            String firstName,
            String lastName,
            String email,
            String phoneNumber
    );

    Customer getCustomerById(UUID customerId);
}
