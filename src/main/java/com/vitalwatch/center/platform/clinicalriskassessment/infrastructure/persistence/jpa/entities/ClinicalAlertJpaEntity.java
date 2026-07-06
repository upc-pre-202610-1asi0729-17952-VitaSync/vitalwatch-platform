package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.ClinicalAlertStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * JPA entity for clinical alerts.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clinical_alerts")
public class ClinicalAlertJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 30)
    private RiskLevel severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ClinicalAlertStatus status = ClinicalAlertStatus.ACTIVE;

    @Column(name = "message", nullable = false, length = 300)
    private String message;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    @Column(name = "resolved_by")
    private Long resolvedBy;

    public ClinicalAlertJpaEntity(
            Long organizationId,
            Long userId,
            RiskLevel severity,
            ClinicalAlertStatus status,
            String message,
            OffsetDateTime createdAt
    ) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.severity = severity;
        this.status = status == null ? ClinicalAlertStatus.ACTIVE : status;
        this.message = message;
        this.createdAt = createdAt == null ? OffsetDateTime.now() : createdAt;
    }

    public void updateStatus(
            ClinicalAlertStatus status,
            OffsetDateTime resolvedAt,
            Long resolvedBy
    ) {
        this.status = status;

        if (resolvedAt != null) {
            this.resolvedAt = resolvedAt;
        }

        if (resolvedBy != null) {
            this.resolvedBy = resolvedBy;
        }
    }
}