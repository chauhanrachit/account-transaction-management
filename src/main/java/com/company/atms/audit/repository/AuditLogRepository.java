package com.company.atms.audit.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.company.atms.audit.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}