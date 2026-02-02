package com.company.atms.audit.service;

import java.util.UUID;

import com.company.atms.audit.entity.AuditAction;

public interface AuditService {
	
    void recordEvent(
            AuditAction action,
            String entityName,
            UUID entityId,
            String description
    );
}