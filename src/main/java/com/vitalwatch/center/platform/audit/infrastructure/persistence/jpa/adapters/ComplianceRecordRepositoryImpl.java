package com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.audit.domain.model.aggregates.ComplianceRecord;
import com.vitalwatch.center.platform.audit.domain.repositories.ComplianceRecordRepository;
import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.assemblers.ComplianceRecordPersistenceAssembler;
import com.vitalwatch.center.platform.audit.infrastructure.persistence.jpa.repositories.ComplianceRecordPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for ComplianceRecord.
 */
@Repository
public class ComplianceRecordRepositoryImpl implements ComplianceRecordRepository {

    private final ComplianceRecordPersistenceRepository repository;

    public ComplianceRecordRepositoryImpl(ComplianceRecordPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ComplianceRecord> findById(Long id) {
        return repository.findById(id)
                .map(ComplianceRecordPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<ComplianceRecord> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceIdOrderByRecordedAtDesc(hospitalWorkspaceId)
                .stream()
                .map(ComplianceRecordPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public ComplianceRecord save(ComplianceRecord complianceRecord) {
        var savedEntity = repository.save(
                ComplianceRecordPersistenceAssembler.toPersistenceFromDomain(complianceRecord)
        );

        return ComplianceRecordPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}