package com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.adapters;

import com.vitalwatch.center.platform.clinicalrisk.domain.model.aggregates.ClinicalRiskAssessment;
import com.vitalwatch.center.platform.clinicalrisk.domain.repositories.ClinicalRiskAssessmentRepository;
import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.assemblers.ClinicalRiskAssessmentPersistenceAssembler;
import com.vitalwatch.center.platform.clinicalrisk.infrastructure.persistence.jpa.repositories.ClinicalRiskAssessmentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter for ClinicalRiskAssessment.
 */
@Repository
public class ClinicalRiskAssessmentRepositoryImpl implements ClinicalRiskAssessmentRepository {

    private final ClinicalRiskAssessmentPersistenceRepository repository;

    public ClinicalRiskAssessmentRepositoryImpl(ClinicalRiskAssessmentPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ClinicalRiskAssessment> findById(Long id) {
        return repository.findById(id)
                .map(ClinicalRiskAssessmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<ClinicalRiskAssessment> findLatestByUserAccountId(Long userAccountId) {
        return repository.findFirstByUserAccountIdOrderByAssessedAtDesc(userAccountId)
                .map(ClinicalRiskAssessmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<ClinicalRiskAssessment> findAllByHospitalWorkspaceId(Long hospitalWorkspaceId) {
        return repository.findAllByHospitalWorkspaceIdOrderByAssessedAtDesc(hospitalWorkspaceId)
                .stream()
                .map(ClinicalRiskAssessmentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public ClinicalRiskAssessment save(ClinicalRiskAssessment clinicalRiskAssessment) {
        var savedEntity = repository.save(
                ClinicalRiskAssessmentPersistenceAssembler.toPersistenceFromDomain(clinicalRiskAssessment)
        );
        return ClinicalRiskAssessmentPersistenceAssembler.toDomainFromPersistence(savedEntity);
    }
}