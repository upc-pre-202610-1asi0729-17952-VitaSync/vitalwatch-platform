package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.shifts.domain.model.enums.ShiftAssignmentStatus;
import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.entities.ShiftAssignmentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data repository for shift assignments.
 */
public interface ShiftAssignmentPersistenceRepository extends JpaRepository<ShiftAssignmentPersistenceEntity, Long> {

    List<ShiftAssignmentPersistenceEntity> findAllByWorkShiftIdOrderByAssignedAtDesc(Long workShiftId);

    List<ShiftAssignmentPersistenceEntity> findAllByUserAccountIdOrderByAssignedAtDesc(Long userAccountId);

    boolean existsByUserAccountIdAndStatusIn(
            Long userAccountId,
            Collection<ShiftAssignmentStatus> statuses
    );
}