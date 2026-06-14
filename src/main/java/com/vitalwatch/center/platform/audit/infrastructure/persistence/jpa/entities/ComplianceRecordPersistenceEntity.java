package com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.ComplianceStatus;
import com.vitalwatch.center.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * JPA persistence entity for compliance records.
 */
@Entity
@Table(name = "compliance_records")
public class ComplianceRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "hospital_workspace_id", nullable = false)
    private Long hospitalWorkspaceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private AuditResourceType resourceType;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceStatus status;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "reviewed_by_user_account_id")
    private Long reviewedByUserAccountId;

    @Column(name = "review_notes", length = 1000)
    private String reviewNotes;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    public ComplianceRecordPersistenceEntity() {
    }

    public Long getHospitalWorkspaceId() {
        return hospitalWorkspaceId;
    }

    public void setHospitalWorkspaceId(Long hospitalWorkspaceId) {
        this.hospitalWorkspaceId = hospitalWorkspaceId;
    }

    public AuditResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(AuditResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public ComplianceStatus getStatus() {
        return status;
    }

    public void setStatus(ComplianceStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getReviewedByUserAccountId() {
        return reviewedByUserAccountId;
    }

    public void setReviewedByUserAccountId(Long reviewedByUserAccountId) {
        this.reviewedByUserAccountId = reviewedByUserAccountId;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}