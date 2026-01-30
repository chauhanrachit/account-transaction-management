package com.company.atms.transaction.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.company.atms.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
