package com.vitalwatch.center.platform.audit.domain.model.aggregates;

import com.vitalwatch.center.platform.audit.domain.model.commands.RecordComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.domain.model.commands.ReviewComplianceRecordCommand;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.ComplianceStatus;
import com.vitalwatch.center.platform.audit.domain.model.valueobjects.AuditDescription;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents a compliance record for a system resource.
 */
public class ComplianceRecord extends AbstractDomainAggregateRoot<ComplianceRecord> {

    private Long id;
    private Long hospitalWorkspaceId;
    private AuditResourceType resourceType;
    private Long resourceId;
    private ComplianceStatus status;
    private AuditDescription description;
    private Long reviewedByUserAccountId;
    private String reviewNotes;
    private Instant recordedAt;
    private Instant reviewedAt;

    public ComplianceRecord(
            Long id,
            Long hospitalWorkspaceId,
            AuditResourceType resourceType,
            Long resourceId,
            ComplianceStatus status,
            AuditDescription description,
            Long reviewedByUserAccountId,
            String reviewNotes,
            Instant recordedAt,
            Instant reviewedAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.resourceType = Objects.requireNonNull(resourceType, "resourceType must not be null");
        this.resourceId = validatePositiveId(resourceId, "resourceId");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.reviewedByUserAccountId = reviewedByUserAccountId;
        this.reviewNotes = reviewNotes;
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt must not be null");
        this.reviewedAt = reviewedAt;
    }

    public ComplianceRecord(RecordComplianceRecordCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.resourceType(),
                command.resourceId(),
                command.status(),
                new AuditDescription(command.description()),
                null,
                null,
                Instant.now(),
                null
        );
    }

    public void review(ReviewComplianceRecordCommand command) {
        if (this.status == ComplianceStatus.REVIEWED) {
            throw new IllegalStateException("Compliance record is already reviewed");
        }

        if (command.reviewNotes() == null || command.reviewNotes().isBlank()) {
            throw new IllegalArgumentException("Review notes must not be null or blank");
        }

        this.status = ComplianceStatus.REVIEWED;
        this.reviewedByUserAccountId = validatePositiveId(command.reviewedByUserAccountId(), "reviewedByUserAccountId");
        this.reviewNotes = command.reviewNotes().trim();
        this.reviewedAt = Instant.now();
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

    public AuditResourceType getResourceType() {
        return resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public ComplianceStatus getStatus() {
        return status;
    }

    public AuditDescription getDescriptionValue() {
        return description;
    }

    public String getDescription() {
        return description.value();
    }

    public Long getReviewedByUserAccountId() {
        return reviewedByUserAccountId;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }
}