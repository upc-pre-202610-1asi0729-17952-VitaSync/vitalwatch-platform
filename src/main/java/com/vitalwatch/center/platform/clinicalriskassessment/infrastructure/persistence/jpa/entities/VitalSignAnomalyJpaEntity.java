package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.RiskLevel;
import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.VitalSignAnomalyStatus;
import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.VitalSignAnomalyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * JPA entity for vital sign anomalies.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vital_sign_anomalies")
public class VitalSignAnomalyJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private VitalSignAnomalyType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 30)
    private RiskLevel severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private VitalSignAnomalyStatus status = VitalSignAnomalyStatus.OPEN;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "threshold", nullable = false)
    private Double threshold;

    @Column(name = "message", nullable = false, length = 300)
    private String message;

    @Column(name = "detected_at", nullable = false)
    private OffsetDateTime detectedAt = OffsetDateTime.now();

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    public VitalSignAnomalyJpaEntity(
            Long organizationId,
            Long userId,
            VitalSignAnomalyType type,
            RiskLevel severity,
            VitalSignAnomalyStatus status,
            Double value,
            Double threshold,
            String message,
            OffsetDateTime detectedAt
    ) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.type = type;
        this.severity = severity;
        this.status = status == null ? VitalSignAnomalyStatus.OPEN : status;
        this.value = value;
        this.threshold = threshold;
        this.message = message;
        this.detectedAt = detectedAt == null ? OffsetDateTime.now() : detectedAt;
    }

    public void updateStatus(
            VitalSignAnomalyStatus status,
            OffsetDateTime reviewedAt,
            Long reviewedBy
    ) {
        this.status = status;
        this.reviewedAt = reviewedAt;
        this.reviewedBy = reviewedBy;
    }
}