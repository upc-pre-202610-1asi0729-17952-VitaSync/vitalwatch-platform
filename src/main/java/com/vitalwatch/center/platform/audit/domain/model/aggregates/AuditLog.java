package com.vitalwatch.center.platform.audit.domain.model.aggregates;

import com.vitalwatch.center.platform.audit.domain.model.commands.RecordAuditLogCommand;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditActionType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditResourceType;
import com.vitalwatch.center.platform.audit.domain.model.enums.AuditSeverity;
import com.vitalwatch.center.platform.audit.domain.model.valueobjects.AuditDescription;
import com.vitalwatch.center.platform.audit.domain.model.valueobjects.IpAddress;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root that represents one auditable system action.
 */
public class AuditLog extends AbstractDomainAggregateRoot<AuditLog> {

    private Long id;
    private Long hospitalWorkspaceId;
    private Long actorUserAccountId;
    private AuditActionType actionType;
    private AuditResourceType resourceType;
    private Long resourceId;
    private AuditSeverity severity;
    private AuditDescription description;
    private IpAddress ipAddress;
    private Instant occurredAt;

    public AuditLog(
            Long id,
            Long hospitalWorkspaceId,
            Long actorUserAccountId,
            AuditActionType actionType,
            AuditResourceType resourceType,
            Long resourceId,
            AuditSeverity severity,
            AuditDescription description,
            IpAddress ipAddress,
            Instant occurredAt
    ) {
        this.id = id;
        this.hospitalWorkspaceId = validatePositiveId(hospitalWorkspaceId, "hospitalWorkspaceId");
        this.actorUserAccountId = validatePositiveId(actorUserAccountId, "actorUserAccountId");
        this.actionType = Objects.requireNonNull(actionType, "actionType must not be null");
        this.resourceType = Objects.requireNonNull(resourceType, "resourceType must not be null");
        this.resourceId = validatePositiveId(resourceId, "resourceId");
        this.severity = Objects.requireNonNull(severity, "severity must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.ipAddress = Objects.requireNonNull(ipAddress, "ipAddress must not be null");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }

    public AuditLog(RecordAuditLogCommand command) {
        this(
                null,
                command.hospitalWorkspaceId(),
                command.actorUserAccountId(),
                command.actionType(),
                command.resourceType(),
                command.resourceId(),
                command.severity(),
                new AuditDescription(command.description()),
                new IpAddress(command.ipAddress()),
                Instant.now()
        );
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

    public Long getActorUserAccountId() {
        return actorUserAccountId;
    }

    public AuditActionType getActionType() {
        return actionType;
    }

    public AuditResourceType getResourceType() {
        return resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public AuditSeverity getSeverity() {
        return severity;
    }

    public AuditDescription getDescriptionValue() {
        return description;
    }

    public String getDescription() {
        return description.value();
    }

    public IpAddress getIpAddressValue() {
        return ipAddress;
    }

    public String getIpAddress() {
        return ipAddress.value();
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}