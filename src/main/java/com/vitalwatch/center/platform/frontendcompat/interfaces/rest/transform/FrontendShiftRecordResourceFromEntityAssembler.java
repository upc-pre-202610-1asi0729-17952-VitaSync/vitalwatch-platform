package com.vitalwatch.center.platform.frontendcompat.interfaces.rest.transform;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendShiftRecordResource;
import com.vitalwatch.center.platform.shifts.domain.model.aggregates.WorkShift;

import java.time.Instant;

/**
 * Assembler to expose WorkShift using the contract expected by the Angular frontend.
 */
public final class FrontendShiftRecordResourceFromEntityAssembler {

    private FrontendShiftRecordResourceFromEntityAssembler() {
    }

    public static FrontendShiftRecordResource toResourceFromEntity(WorkShift entity) {
        return toResourceFromEntity(entity, resolveUserId(entity), null, null, null);
    }

    public static FrontendShiftRecordResource toResourceFromEntity(
            WorkShift entity,
            Long userId,
            String overrideStatus,
            Instant overrideCheckInAt,
            Instant overrideCheckOutAt
    ) {
        var frontendStatus = overrideStatus != null && !overrideStatus.isBlank()
                ? overrideStatus
                : toFrontendStatus(entity.getStatus().name());

        var checkInAt = overrideCheckInAt != null
                ? overrideCheckInAt
                : ("COMPLETED".equals(frontendStatus) ? entity.getStartsAt() : null);

        var checkOutAt = overrideCheckOutAt != null
                ? overrideCheckOutAt
                : ("COMPLETED".equals(frontendStatus) ? entity.getCompletedAt() : null);

        return new FrontendShiftRecordResource(
                entity.getId(),
                entity.getHospitalWorkspaceId(),
                entity.getHospitalWorkspaceId(),
                userId,
                userId,
                workAreaIdFromName(entity.getWorkArea()),
                entity.getWorkArea(),
                entity.getShiftType().name(),
                toFrontendShiftType(entity.getShiftType().name()),
                frontendStatus,
                entity.getStartsAt(),
                entity.getEndsAt(),
                checkInAt,
                checkOutAt,
                entity.getStartsAt(),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getEndsAt(),
                entity.getCompletedByUserAccountId(),
                entity.getCancelledByUserAccountId(),
                entity.getCancellationReason(),
                entity.getCompletedAt(),
                entity.getCancelledAt()
        );
    }

    private static Long resolveUserId(WorkShift entity) {
        if (entity.getCompletedByUserAccountId() != null) {
            return entity.getCompletedByUserAccountId();
        }
        if (entity.getCancelledByUserAccountId() != null) {
            return entity.getCancelledByUserAccountId();
        }
        return 1L;
    }

    private static String toFrontendStatus(String status) {
        return switch (status) {
            case "PLANNED" -> "SCHEDULED";
            case "ACTIVE" -> "IN_PROGRESS";
            case "COMPLETED" -> "COMPLETED";
            case "CANCELLED" -> "CANCELLED";
            default -> "SCHEDULED";
        };
    }

    private static String toFrontendShiftType(String type) {
        return "NIGHT".equals(type) ? "NIGHT" : "DAY";
    }

    private static Long workAreaIdFromName(String workArea) {
        if (workArea == null || workArea.isBlank()) {
            return 1L;
        }
        return Math.abs((long) workArea.trim().toLowerCase().hashCode() % 1000) + 1L;
    }
}