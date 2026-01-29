package com.company.atms.audit.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.company.atms.account.entity.Account;
import com.company.atms.transaction.entity.TransactionStatus;
import com.company.atms.transaction.entity.TransactionType;

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
@Table(
        name = "audit_logs",
        indexes = {
                @Index(name = "idx_audit_entity", columnList = "entity_name"),
                @Index(name = "idx_audit_entity_id", columnList = "entity_id"),
                @Index(name = "idx_audit_created_at", columnList = "created_at")
        }
)
@Getter
@NoArgsConstructor
public class AuditLog {
/*
 * Tracks important actions.
 * Entity: AuditLogEntity
 * Fields:
	id
	entityType
	entityId
	action
	performedBy
	timestamp
	metadata (JSON / String)
*/
	@Id
	@UuidGenerator
	@Setter(value = AccessLevel.NONE)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

    @Column(name = "entity_name", nullable = false, length = 100)
    private String entityName;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "action", nullable = false, length = 50)
    private String action; // CREATE / UPDATE / DELETE / STATUS_CHANGE

    @Column(name = "performed_by", length = 150)
    private String performedBy;

    @Column(name = "old_value", columnDefinition = "text")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "text")
    private String newValue;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
