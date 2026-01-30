package com.company.atms.audit.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs", indexes = { @Index(name = "idx_audit_entity", columnList = "entity_name"),
		@Index(name = "idx_audit_entity_id", columnList = "entity_id"),
		@Index(name = "idx_audit_created_at", columnList = "created_at") })
@Getter
@NoArgsConstructor
public class AuditLog {
	/*
	 * Tracks important actions. Entity: AuditLogEntity 
	 * Fields: 
	 * id entityType entityId action performedBy timestamp metadata (JSON / String)
	 */
	@Id
	@UuidGenerator
	@Setter(AccessLevel.NONE)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "entity_name", nullable = false, length = 100)
	private String entityName;

	@Column(name = "entity_id", nullable = false)
	private UUID entityId;

	@Enumerated(EnumType.STRING)
	@Column(name = "action", nullable = false, length = 50)
	private AuditAction action;

	@Column(name = "performed_by", length = 150)
	private String performedBy;

	@Column(name = "old_value", columnDefinition = "text")
	private String oldValue;

	@Column(name = "new_value", columnDefinition = "text")
	private String newValue;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	// Domain constructor — enforce invariants
	public AuditLog(String entityName, UUID entityId, AuditAction action, String performedBy, String oldValue,
			String newValue) {
		if (entityName == null || entityName.isBlank()) {
			throw new IllegalArgumentException("Entity name is required");
		}
		if (entityId == null) {
			throw new IllegalArgumentException("Entity ID is required");
		}
		if (action == null) {
			throw new IllegalArgumentException("Audit action is required");
		}

		this.entityName = entityName;
		this.entityId = entityId;
		this.action = action;
		this.performedBy = performedBy;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AuditLog))
			return false;
		AuditLog other = (AuditLog) o;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "AuditLog{" + "id=" + id + ", entityName='" + entityName + '\'' + ", entityId=" + entityId + ", action="
				+ action + ", performedBy='" + performedBy + '\'' + ", createdAt=" + createdAt + '}';
	}
}
