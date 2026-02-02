package com.company.atms.audit.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.atms.audit.entity.AuditAction;
import com.company.atms.audit.entity.AuditLog;
import com.company.atms.audit.repository.AuditLogRepository;

@Service
public class AuditServiceImpl implements AuditService {

	private final AuditLogRepository auditLogRepository;

    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void recordEvent(AuditAction action, String entityName, UUID entityId, String description) {

        try {
        	AuditLog log = new AuditLog(
                    entityName,
                    entityId,
                    action,
                    null,          // performedBy (will be added in Phase 4 - auth)
                    null,          // oldValue (future)
                    description    // newValue / description
            );

            auditLogRepository.save(log);

        } catch (Exception ex) {
            // VERY IMPORTANT: audit must not break business flow
            // this to be logged properly later
        }
    }

}
