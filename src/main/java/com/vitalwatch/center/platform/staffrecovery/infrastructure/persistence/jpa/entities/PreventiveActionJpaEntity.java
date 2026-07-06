package com.vitalwatch.center.platform.staffrecovery.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionStatus;
import com.vitalwatch.center.platform.staffrecovery.domain.model.enums.PreventiveActionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * JPA entity for preventive recovery actions.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "preventive_actions")
public class PreventiveActionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "supervisor_id", nullable = false)
    private Long supervisorId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private PreventiveActionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PreventiveActionStatus status = PreventiveActionStatus.PENDING;

    @Column(name = "notes", nullable = false, length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    public PreventiveActionJpaEntity(
            Long organizationId,
            Long supervisorId,
            Long userId,
            PreventiveActionType type,
            PreventiveActionStatus status,
            String notes,
            OffsetDateTime createdAt,
            OffsetDateTime completedAt
    ) {
        this.organizationId = organizationId;
        this.supervisorId = supervisorId;
        this.userId = userId;
        this.type = type;
        this.status = status == null ? PreventiveActionStatus.PENDING : status;
        this.notes = notes;
        this.createdAt = createdAt == null ? OffsetDateTime.now() : createdAt;
        this.completedAt = completedAt;
    }

    public void updateStatus(
            PreventiveActionStatus status,
            OffsetDateTime completedAt
    ) {
        this.status = status;

        if (status == PreventiveActionStatus.COMPLETED) {
            this.completedAt = completedAt == null ? OffsetDateTime.now() : completedAt;
        }

        if (status == PreventiveActionStatus.CANCELLED) {
            this.completedAt = null;
        }
    }
}