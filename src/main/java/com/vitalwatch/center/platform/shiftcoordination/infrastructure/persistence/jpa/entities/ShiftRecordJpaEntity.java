package com.vitalwatch.center.platform.shiftcoordination.infrastructure.persistence.jpa.entities;

import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.ShiftStatus;
import com.vitalwatch.center.platform.shiftcoordination.domain.model.enums.ShiftType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * JPA entity for scheduled and completed medical shifts.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shift_records")
public class ShiftRecordJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "work_area_id", nullable = false)
    private Long workAreaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private ShiftType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ShiftStatus status = ShiftStatus.SCHEDULED;

    @Column(name = "scheduled_start", nullable = false)
    private OffsetDateTime scheduledStart;

    @Column(name = "scheduled_end", nullable = false)
    private OffsetDateTime scheduledEnd;

    @Column(name = "check_in_at")
    private OffsetDateTime checkInAt;

    @Column(name = "check_out_at")
    private OffsetDateTime checkOutAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ShiftRecordJpaEntity(
            Long organizationId,
            Long userId,
            Long workAreaId,
            ShiftType type,
            ShiftStatus status,
            OffsetDateTime scheduledStart,
            OffsetDateTime scheduledEnd,
            OffsetDateTime checkInAt,
            OffsetDateTime checkOutAt
    ) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.workAreaId = workAreaId;
        this.type = type;
        this.status = status == null ? ShiftStatus.SCHEDULED : status;
        this.scheduledStart = scheduledStart;
        this.scheduledEnd = scheduledEnd;
        this.checkInAt = checkInAt;
        this.checkOutAt = checkOutAt;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(
            ShiftStatus status,
            OffsetDateTime checkInAt,
            OffsetDateTime checkOutAt
    ) {
        this.status = status;

        if (checkInAt != null) {
            this.checkInAt = checkInAt;
        }

        if (checkOutAt != null) {
            this.checkOutAt = checkOutAt;
        }

        this.updatedAt = LocalDateTime.now();
    }
}