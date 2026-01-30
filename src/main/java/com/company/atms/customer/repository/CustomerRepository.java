package com.company.atms.customer.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.company.atms.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}