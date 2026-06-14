package com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.shifts.infrastructure.persistence.jpa.entities.WorkShiftPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for work shifts.
 */
public interface WorkShiftPersistenceRepository extends JpaRepository<WorkShiftPersistenceEntity, Long> {

    List<WorkShiftPersistenceEntity> findAllByHospitalWorkspaceIdOrderByStartsAtDesc(Long hospitalWorkspaceId);
}