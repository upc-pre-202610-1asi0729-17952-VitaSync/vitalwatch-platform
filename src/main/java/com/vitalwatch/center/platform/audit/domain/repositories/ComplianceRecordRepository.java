package com.vitalwatch.center.platform.audit.domain.repositories;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for compliance records.
 */
public interface ComplianceRecordRepository {

    Optional<ComplianceRecord> findById(Long id);

    List<ComplianceRecord> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId);

    ComplianceRecord save(ComplianceRecord complianceRecord);
}