package com.vitalwatch.center.platform.clinicalriskassessment.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.clinicalriskassessment.domain.model.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * JPA entity for fatigue and clinical risk assessments.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_assessments")
public class RiskAssessmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "fatigue_level", nullable = false)
    private Integer fatigueLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 30)
    private RiskLevel riskLevel;

    @Column(name = "heart_rate", nullable = false)
    private Integer heartRate;

    @Column(name = "hrv", nullable = false)
    private Integer hrv;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt = OffsetDateTime.now();

    public RiskAssessmentJpaEntity(
            Long organizationId,
            Long userId,
            Integer fatigueLevel,
            RiskLevel riskLevel,
            Integer heartRate,
            Integer hrv,
            OffsetDateTime lastUpdatedAt
    ) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.fatigueLevel = fatigueLevel;
        this.riskLevel = riskLevel;
        this.heartRate = heartRate;
        this.hrv = hrv;
        this.lastUpdatedAt = lastUpdatedAt == null ? OffsetDateTime.now() : lastUpdatedAt;
    }
}