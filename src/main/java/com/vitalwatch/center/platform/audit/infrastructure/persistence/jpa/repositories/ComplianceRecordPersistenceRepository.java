package com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.repositories;

import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.entities.ComplianceRecordPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for compliance records.
 */
public interface ComplianceRecordPersistenceRepository extends JpaRepository<ComplianceRecordPersistenceEntity, Long> {

    List<ComplianceRecordPersistenceEntity> findAllByHospitalWorkspaceIdOrderByRecordedAtDesc(Long hospitalWorkspaceId);
}