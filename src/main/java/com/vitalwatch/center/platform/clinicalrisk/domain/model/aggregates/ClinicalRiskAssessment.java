package com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.commands.CreateClinicalRiskAssessmentCommand;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.AssessmentStatus;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.enums.RiskLevel;
import com.vitalwatch.center.platform.clinicalrisk.domain.model.valueobjects.FatigueScore;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents a fatigue and clinical risk assessment.
 */
public class ClinicalRiskAssessment extends AbstractDomainAggregateRoot<ClinicalRiskAssessment> {

    private Long id;
    private Long hospitalWorkspaceId;
    private Long userAccountId;
    private Long vitalSignReadingId;
    private FatigueScore fatigueScore;
    private RiskLevel riskLevel;
    private AssessmentStatus status;
    private Instant assessedAt;
    private Instant reviewedAt;
    private Instant escalatedAt;
    private Instant closedAt;

    public ClinicalRiskAssessment(
            Long id,
            Long hospitalWorkspaceId,
            Long userAccountId,
            Long vitalSignReadingId,
            FatigueScore fatigueScore,
            RiskLevel riskLevel,
            AssessmentStatus status,
            Instant assessedAt,
            Instant reviewedAt,
            Instant escalatedAt,
            Instant closedAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.userAccountId = validatePositiveId(userAccountId, "userAccountId");
        this.vitalSignReadingId = validatePositiveId(vitalSignReadingId, "vitalSignReadingId");
        this.fatigueScore = Objects.requireNonNull(fatigueScore, "fatigueScore must not be null");
        this.riskLevel = Objects.requireNonNull(riskLevel, "riskLevel must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.assessedAt = Objects.requireNonNull(assessedAt, "assessedAt must not be null");
        this.reviewedAt = reviewedAt;
        this.escalatedAt = escalatedAt;
        this.closedAt = closedAt;
    }

    public ClinicalRiskAssessment(CreateClinicalRiskAssessmentCommand command, VitalSignReading reading) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.userAccountId(),
                command.vitalSignReadingId(),
                calculateFatigueScore(reading),
                calculateRiskLevel(calculateFatigueScore(reading)),
                AssessmentStatus.CREATED,
                Instant.now(),
                null,
                null,
                null
        );
    }

    public void review() {
        if (this.status == AssessmentStatus.CLOSED) {
            throw new IllegalStateException("Closed assessments cannot be reviewed");
        }
        this.status = AssessmentStatus.REVIEWED;
        this.reviewedAt = Instant.now();
    }

    public void escalate() {
        if (this.status == AssessmentStatus.CLOSED) {
            throw new IllegalStateException("Closed assessments cannot be escalated");
        }
        if (this.riskLevel == RiskLevel.LOW || this.riskLevel == RiskLevel.MODERATE) {
            throw new IllegalStateException("Only high or critical risk assessments can be escalated");
        }
        this.status = AssessmentStatus.ESCALATED;
        this.escalatedAt = Instant.now();
    }

    public void close() {
        this.status = AssessmentStatus.CLOSED;
        this.closedAt = Instant.now();
    }

    private static FatigueScore calculateFatigueScore(VitalSignReading reading) {
        int score = 0;

        double sleepHours = reading.getSleepHoursLast24h();
        double shiftHours = reading.getShiftHoursLast24h();
        int heartRate = reading.getHeartRateBpm();
        int selfReportedFatigue = reading.getSelfReportedFatigueLevel();

        if (sleepHours < 4) {
            score += 35;
        } else if (sleepHours < 6) {
            score += 20;
        } else if (sleepHours < 7) {
            score += 10;
        }

        if (shiftHours > 16) {
            score += 30;
        } else if (shiftHours > 12) {
            score += 20;
        } else if (shiftHours > 8) {
            score += 10;
        }

        if (heartRate > 120) {
            score += 25;
        } else if (heartRate > 100) {
            score += 15;
        } else if (heartRate < 50) {
            score += 10;
        }

        score += selfReportedFatigue * 8;

        return new FatigueScore(Math.min(score, 100));
    }

    private static RiskLevel calculateRiskLevel(FatigueScore fatigueScore) {
        int value = fatigueScore.value();

        if (value <= 25) {
            return RiskLevel.LOW;
        }
        if (value <= 50) {
            return RiskLevel.MODERATE;
        }
        if (value <= 75) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.CRITICAL;
    }

    private Long validatePositiveId(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive number");
        }
        return value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public Long getVitalSignReadingId() {
        return vitalSignReadingId;
    }

    public FatigueScore getFatigueScoreValue() {
        return fatigueScore;
    }

    public Integer getFatigueScore() {
        return fatigueScore.value();
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public AssessmentStatus getStatus() {
        return status;
    }

    public Instant getAssessedAt() {
        return assessedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public Instant getEscalatedAt() {
        return escalatedAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }
}