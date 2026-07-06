package com.vitalwatch.center.platform.auditcompliance.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogSeverity;
import com.vitalwatch.center.platform.auditcompliance.domain.model.enums.AuditLogType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * JPA entity for audit logs.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "actor_user_id")
    private Long actorUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private AuditLogType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 30)
    private AuditLogSeverity severity = AuditLogSeverity.INFO;

    @Column(name = "resource_type", nullable = false, length = 80)
    private String resourceType;

    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public AuditLogJpaEntity(
            Long organizationId,
            Long actorUserId,
            AuditLogType type,
            AuditLogSeverity severity,
            String resourceType,
            Long resourceId,
            String description,
            OffsetDateTime createdAt
    ) {
        this.organizationId = organizationId;
        this.actorUserId = actorUserId;
        this.type = type;
        this.severity = severity == null ? AuditLogSeverity.INFO : severity;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.description = description;
        this.createdAt = createdAt == null ? OffsetDateTime.now() : createdAt;
    }
}